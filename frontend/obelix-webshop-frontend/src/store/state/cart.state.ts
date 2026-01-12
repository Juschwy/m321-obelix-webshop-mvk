import { CartItem } from '../../app/webshop/cart/cart.component';

export interface CartState {
  menhirs: CartItem[];
}

export const initialCartState: CartState = {
  menhirs: [],
};

export const getAllMengirsInCart = (state: CartState) => state.menhirs;
