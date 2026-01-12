import { CartState } from '../state/cart.state';

export const getAllMenhirsInCart = (state: { cart: CartState }) =>
  state.cart.menhirs;
