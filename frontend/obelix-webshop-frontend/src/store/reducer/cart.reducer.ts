import { createReducer, on } from '@ngrx/store';

import * as fromCartState from '../state/cart.state';
import * as fromCartActions from '../action/cart.action';

const cartReducer = createReducer(
  fromCartState.initialCartState,

  on(fromCartActions.addItemToCart, (state, { menhirs }) => ({
    ...state,
    menhirs: [...state.menhirs, ...menhirs],
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
      menhir.name === menhirs.name ? { ...menhir } : menhir,
    ),
  })),
);
