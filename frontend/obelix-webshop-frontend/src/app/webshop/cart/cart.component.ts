import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { Component, computed, Inject, OnInit, signal } from '@angular/core';

import * as fromCartActions from '../../../store/action/cart.action';
import * as fromCartSelector from '../../../store/selector/cart.selector';
import {CheckoutComponent} from "../checkout/checkout.component";

export interface CartItem {
  readonly id: string;
  readonly name: string;
  readonly description: string;
  readonly price: number; // stored in dollars without decimals for formatter compatibility
  readonly quantity: number;
  readonly imageUrl: string;
}

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, CheckoutComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  private store$ = Inject(Store);
  private readonly currencyFormatter = new Intl.NumberFormat('de-DE');

  readonly isOpen$$ = signal(false);

  readonly items$$ = signal<CartItem[]>([]);

  readonly isCheckoutOpen$$ = signal(false);

  ngOnInit(): void {
    this.store$
      .select(fromCartSelector.getAllMenhirsInCart)
      .subscribe((menhirs: CartItem[]) => {
        this.items$$.set(menhirs);
      });
  }

  readonly total = computed(() =>
    this.items$$().reduce(
      (runningTotal, item) => runningTotal + item.price * item.quantity,
      0,
    ),
  );

  formatPrice(value: number): string {
    return `$${this.currencyFormatter.format(value)}`;
  }

  increaseQuantity(id: string): void {
    this.updateItem(id, true);
  }

  decreaseQuantity(id: string): void {
    this.updateItem(id, false);
  }

  removeItem(id: string): void {
    this.items$$.update((current) => current.filter((item) => item.id !== id));
  }

  close(): void {
    this.isOpen$$.set(false);
  }

  open(): void {
    this.isOpen$$.set(true);
  }

  private updateItem(id: string, increase: boolean): void {
    const currentItems = this.items$$();
    const selectedIndex = currentItems.findIndex((item) => item.id === id);

    if (selectedIndex === -1) return;

    const selectedMenhir = { ...currentItems[selectedIndex] };
    selectedMenhir.quantity += increase ? 1 : -1;

    if (selectedMenhir.quantity <= 0) {
      this.store$.dispatch(fromCartActions.removeItemFromCart({ id }));
      return;
    }

    this.store$.dispatch(
      fromCartActions.updateItemFromCart({
        menhirs: selectedMenhir,
      }),
    );
  }

  openCheckout(): void {
    console.log('open checkout');
    if (this.items$$().length === 0) return;
    this.isCheckoutOpen$$.set(true);
  }

  closeCheckout(): void {
    this.isCheckoutOpen$$.set(false);
  }

  onPurchased(): void {
    this.close();
  }
}
