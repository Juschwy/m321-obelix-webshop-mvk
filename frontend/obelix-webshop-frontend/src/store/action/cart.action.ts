import { createAction, props } from '@ngrx/store';
import { CartItem } from '../../app/webshop/cart/cart.component';

export const addItemToCart = createAction(
  '[Cart] Add Items to Cart',
  props<{ menhirs: CartItem[] }>(),
);

export const removeItemFromCart = createAction(
  '[Cart] Remove Item from Cart',
  props<{ id: string }>(),
);

export const clearCart = createAction('[Cart] Clear Cart');

export const updateItemFromCart = createAction(
  '[Cart] Update Item Quantity',
  props<{ menhirs: { name: string; quantity: number } }>(),
);
