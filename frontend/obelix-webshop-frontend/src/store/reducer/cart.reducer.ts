import { createReducer, on } from '@ngrx/store';

import * as fromCartState from '../state/cart.state';
import * as fromCartActions from '../action/cart.action';

export const cartReducer = createReducer(
  fromCartState.initialCartState,

  on(fromCartActions.addItemToCart, (state, { menhirs }) => ({
    ...state,
    menhirs: menhirs.reduce((next, incoming) => {
      const existingIndex = next.findIndex((item) => item.id === incoming.id);
      if (existingIndex === -1) {
        next.push(incoming);
        return next;
      }
      next[existingIndex] = {
        ...next[existingIndex],
        quantity: next[existingIndex].quantity + incoming.quantity,
      };
      return next;
    }, [...state.menhirs]),
  })),
  on(fromCartActions.removeItemFromCart, (state, { id }) => ({
    ...state,
    menhirs: state.menhirs.filter((menhir) => menhir.id !== id),
  })),
  on(fromCartActions.clearCart, (state) => ({
    ...state,
    menhirs: [],
  })),
  on(fromCartActions.updateItemFromCart, (state, { menhirs }) => ({
    ...state,
    menhirs: state.menhirs.map((menhir) =>
      menhir.id === menhirs.id
        ? { ...menhir, quantity: menhirs.quantity }
        : menhir,
    ),
  })),
);
