import { Component } from '@angular/core';
import {HeaderComponent} from "./shared/header/header.component";
import {WebshopComponent} from "./webshop/webshop.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, WebshopComponent],
  templateUrl: './app.component.html',
})
export class AppComponent {}
