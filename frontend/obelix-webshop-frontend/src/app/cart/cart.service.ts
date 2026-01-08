import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { MenhirDto } from '../models/menhir.dto';

export interface CartItem {
  menhir: MenhirDto;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems$ = new BehaviorSubject<CartItem[]>([]);

  constructor() {
    // Load cart from localStorage on init
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      try {
        this.cartItems$.next(JSON.parse(savedCart));
      } catch (e) {
        console.error('Error loading cart from localStorage', e);
      }
    }
  }

  getCartItems(): Observable<CartItem[]> {
    return this.cartItems$.asObservable();
  }

  getCartItemsValue(): CartItem[] {
    return this.cartItems$.value;
  }

  addItem(menhir: MenhirDto): void {
    const currentItems = this.cartItems$.value;
    const existingItem = currentItems.find(item => item.menhir.id === menhir.id);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      currentItems.push({ menhir, quantity: 1 });
    }

    this.cartItems$.next([...currentItems]);
    this.saveToLocalStorage();
  }

  removeItem(menhirId: string): void {
    const currentItems = this.cartItems$.value.filter(item => item.menhir.id !== menhirId);
    this.cartItems$.next(currentItems);
    this.saveToLocalStorage();
  }

  updateQuantity(menhirId: string, quantity: number): void {
    if (quantity <= 0) {
      this.removeItem(menhirId);
      return;
    }

    const currentItems = this.cartItems$.value;
    const item = currentItems.find(item => item.menhir.id === menhirId);
    if (item) {
      item.quantity = quantity;
      this.cartItems$.next([...currentItems]);
      this.saveToLocalStorage();
    }
  }

  getTotal(): number {
    return this.cartItems$.value.reduce((total, item) => {
      const price = this.getMenhirPrice(item.menhir);
      return total + (price * item.quantity);
    }, 0);
  }

  getItemCount(): number {
    return this.cartItems$.value.reduce((count, item) => count + item.quantity, 0);
  }

  clearCart(): void {
    this.cartItems$.next([]);
    this.saveToLocalStorage();
  }

  private getMenhirPrice(menhir: MenhirDto): number {
    const basePrice = menhir.weight * 1500;
    const decorativenessMultiplier: { [key: string]: number } = {
      'PLAIN': 1.0,
      'SIMPLE': 1.2,
      'DECORATED': 1.5,
      'ORNATE': 2.0,
      'MASTERWORK': 3.0
    };
    const multiplier = decorativenessMultiplier[menhir.decorativeness] || 1.0;
    return Math.round(basePrice * multiplier);
  }

  private saveToLocalStorage(): void {
    try {
      localStorage.setItem('cart', JSON.stringify(this.cartItems$.value));
    } catch (e) {
      console.error('Error saving cart to localStorage', e);
    }
  }
}

