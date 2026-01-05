import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';

interface CartItem {
  readonly id: string;
  readonly name: string;
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
export class CartComponent {
  private readonly currencyFormatter = new Intl.NumberFormat('de-DE');

  readonly isOpen = signal(false);

  readonly items = signal<CartItem[]>([
    {
      id: 'ancient-monument-stone',
      name: 'Ancient Monument Stone',
      price: 6200,
      quantity: 2,
      imageUrl:
        'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=240&q=80',
    },
    {
      id: 'mystical-monolith',
      name: 'Mystical Monolith',
      price: 5700,
      quantity: 1,
      imageUrl:
        'https://images.unsplash.com/photo-1548710840-8be002e7cd05?auto=format&fit=crop&w=240&q=80',
    },
    {
      id: 'premium-granite-monolith',
      name: 'Premium Granite Monolith',
      price: 8900,
      quantity: 1,
      imageUrl:
        'https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=240&q=80',
    },
  ]);

  readonly total = computed(() =>
    this.items().reduce(
      (runningTotal, item) => runningTotal + item.price * item.quantity,
      0,
    ),
  );

  formatPrice(value: number): string {
    return `$${this.currencyFormatter.format(value)}`;
  }

  increaseQuantity(id: string): void {
    this.updateItem(id, (item) => ({
      ...item,
      quantity: item.quantity + 1,
    }));
  }

  decreaseQuantity(id: string): void {
    this.updateItem(id, (item) => ({
      ...item,
      quantity: Math.max(1, item.quantity - 1),
    }));
  }

  removeItem(id: string): void {
    this.items.update((current) => current.filter((item) => item.id !== id));
  }

  close(): void {
    this.isOpen.set(false);
  }

  open(): void {
    this.isOpen.set(true);
  }

  private updateItem(
    id: string,
    updater: (item: CartItem) => CartItem,
  ): void {
    this.items.update((current) =>
      current.map((item) => (item.id === id ? updater(item) : item)),
    );
  }
}
