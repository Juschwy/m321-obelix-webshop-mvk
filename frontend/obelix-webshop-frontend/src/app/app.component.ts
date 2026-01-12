import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {HeaderComponent} from "../webshop/header/header.component";
import { CartComponent } from './webshop/cart/cart.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent],
  templateUrl: './app.component.html',
  imports: [CartComponent],
    templateUrl: './app.component.html',
})
export class AppComponent {}
