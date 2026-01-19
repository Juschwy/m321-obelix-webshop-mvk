import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { forkJoin } from 'rxjs';

import * as fromCartActions from '../../../store/action/cart.action';
import * as fromCartSelector from '../../../store/selector/cart.selector';
import { BasketControllerService } from '../../../api';
import { CartUiService } from './cart-ui.service';

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
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  private readonly store$ = inject(Store);
  private readonly basketService = inject(BasketControllerService);
  private readonly cartUi = inject(CartUiService);
  private readonly currencyFormatter = new Intl.NumberFormat('de-DE');

  readonly isOpen$$ = this.cartUi.isOpen$$;

  readonly items$$ = signal<CartItem[]>([]);

  ngOnInit(): void {
    this.store$
      .select(fromCartSelector.getAllMenhirsInCart)
      .subscribe((menhirs: CartItem[]) => {
        this.items$$.set(menhirs);
      });
  }

  readonly total = computed(() =>
    this.items$$().reduce(
      (runningTotal: number, item: CartItem) =>
        runningTotal + item.price * item.quantity,
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
    this.store$.dispatch(fromCartActions.removeItemFromCart({ id }));
    this.basketService.offer({ name: id, count: 0 }).subscribe();
  }

  close(): void {
    this.cartUi.close();
  }

  open(): void {
    this.cartUi.open();
  }

  private updateItem(id: string, increase: boolean): void {
    const currentItems = this.items$$();
    const selectedIndex = currentItems.findIndex(
      (item: CartItem) => item.id === id,
    );

    if (selectedIndex === -1) return;

    const selectedMenhir = { ...currentItems[selectedIndex] };
    selectedMenhir.quantity += increase ? 1 : -1;

    if (selectedMenhir.quantity <= 0) {
      this.store$.dispatch(fromCartActions.removeItemFromCart({ id }));
      this.basketService.offer({ name: id, count: 0 }).subscribe();
      return;
    }

    this.store$.dispatch(
      fromCartActions.updateItemFromCart({
        menhirs: { id: selectedMenhir.id, quantity: selectedMenhir.quantity },
      }),
    );
    this.basketService
      .offer({ name: id, count: selectedMenhir.quantity })
      .subscribe();
  }

  checkout(): void {
    const items = this.items$$();
    if (!items.length) return;
    forkJoin(
      items.map((item: CartItem) => this.basketService.exchangeFor(item.id)),
    ).subscribe({
      next: () => {
        this.store$.dispatch(fromCartActions.clearCart());
        this.close();
      },
    });
  }
}
