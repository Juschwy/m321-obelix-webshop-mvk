import { Component, inject, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { CartService } from '../services/cart.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  private readonly cartService = inject(CartService);
  
  protected readonly cartItemCount$$ = toSignal(
    this.cartService.getCartItems().pipe(
      map(items => items.reduce((sum, item) => sum + item.quantity, 0))
    ),
    { initialValue: 0 }
  );
}
