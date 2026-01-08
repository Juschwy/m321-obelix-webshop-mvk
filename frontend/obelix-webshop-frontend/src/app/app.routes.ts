import { Routes } from '@angular/router';
import { AdminListComponent } from './admin/admin-list.component';
import { CartComponent } from './cart/cart.component';

export const routes: Routes = [
  {
    path: '',
    component: AdminListComponent
  },
  {
    path: 'admin',
    component: AdminListComponent
  },
  {
    path: 'cart',
    component: CartComponent
  }
];
