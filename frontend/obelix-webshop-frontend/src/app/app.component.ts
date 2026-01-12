import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {HeaderComponent} from "./webshop/header/header.component";
import {CardComponent} from "./shared/card/card.component";
import { CartComponent } from './webshop/cart/cart.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CardComponent, CartComponent, HeaderComponent],
  templateUrl: './app.component.html',
  // TODO remove if card-call is moved to individual components
  styleUrl: './app.component.scss'
})
export class AppComponent {}
