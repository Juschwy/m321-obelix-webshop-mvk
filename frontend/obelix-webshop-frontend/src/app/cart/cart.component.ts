import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CartService, CartItem } from './cart.service';
import { MenhirDto } from '../models/menhir.dto';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  total: number = 0;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.getCartItems().subscribe(items => {
      this.cartItems = items;
      this.total = this.cartService.getTotal();
    });
  }

  getItemPrice(menhir: MenhirDto): number {
    return this.getMenhirPrice(menhir);
  }

  getFormattedPrice(price: number): string {
    return new Intl.NumberFormat('de-CH', { 
      style: 'currency', 
      currency: 'CHF',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(price);
  }

  getTotalFormatted(): string {
    return this.getFormattedPrice(this.total);
  }

  increaseQuantity(item: CartItem): void {
    this.cartService.updateQuantity(item.menhir.id, item.quantity + 1);
  }

  decreaseQuantity(item: CartItem): void {
    this.cartService.updateQuantity(item.menhir.id, item.quantity - 1);
  }

  removeItem(menhirId: string): void {
    this.cartService.removeItem(menhirId);
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
}
