import { Component, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { CartService, CartItem } from '../services/cart.service';
import { MenhirDto } from '../models/menhir.dto';
import { ChfCurrencyPipe } from '../shared/pipes/chf-currency.pipe';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, ChfCurrencyPipe],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent {
  private readonly cartService = inject(CartService);

  protected readonly cartItems$$ = toSignal(this.cartService.getCartItems(), { initialValue: [] as CartItem[] });

  protected readonly total$$ = computed(() => {
    return this.cartItems$$().reduce((sum, item) => {
      return sum + (this.getMenhirPrice(item.menhir) * item.quantity);
    }, 0);
  });

  protected getItemPrice(menhir: MenhirDto): number {
    return this.getMenhirPrice(menhir);
  }

  protected increaseQuantity(item: CartItem): void {
    this.cartService.updateQuantity(item.menhir.id, item.quantity + 1);
  }

  protected decreaseQuantity(item: CartItem): void {
    this.cartService.updateQuantity(item.menhir.id, item.quantity - 1);
  }

  protected removeItem(menhirId: string): void {
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
