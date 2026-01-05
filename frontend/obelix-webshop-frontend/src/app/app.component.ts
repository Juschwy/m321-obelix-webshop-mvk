import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CartComponent } from './webshop/cart/cart.component';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, CartComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
    title = 'obelix-webshop-frontend';
}
