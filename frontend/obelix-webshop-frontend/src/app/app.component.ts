import { Component } from '@angular/core';
import { CartComponent } from './webshop/cart/cart.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CartComponent],
    templateUrl: './app.component.html',
})
export class AppComponent {}
