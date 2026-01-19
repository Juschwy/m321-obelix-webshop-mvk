import { Component, Inject, input } from '@angular/core';
import { Store } from '@ngrx/store';

import * as fromCartActions from '../../../store/action/cart.action';
import { CartItem } from '../../webshop/cart/cart.component';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss',
  standalone: true,
})
export class CardComponent {
  private store$ = Inject(Store);

  id$$ = input('');
  title$$ = input('');
  description$$ = input('');
  weight$$ = input<number | null>(null);
  size$$ = input<number | null>(null);
  prize$$ = input<number | null>(null);

  addToCard() {
    const menhir: CartItem = {
      id: this.id$$(),
      name: this.title$$(),
      description: this.description$$(),
      price: this.prize$$() ?? 0,
      quantity: 1,
      imageUrl: '',
    };
    this.store$.dispatch(fromCartActions.addItemToCart({ menhirs: [menhir] }));
  }
}
