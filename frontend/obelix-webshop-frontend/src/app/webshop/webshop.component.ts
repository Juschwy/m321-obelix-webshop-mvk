import { Component } from '@angular/core';
import { CardComponent } from '../shared/card/card.component';
import { CartComponent } from './cart/cart.component';
import { MenhirType } from '../shared/model/menhir-type';

@Component({
  selector: 'app-webshop',
  imports: [
    CardComponent,
    CartComponent
  ],
  templateUrl: './webshop.component.html',
  styleUrl: './webshop.component.scss',
})
export class WebshopComponent {
  menhirType = MenhirType;
}
