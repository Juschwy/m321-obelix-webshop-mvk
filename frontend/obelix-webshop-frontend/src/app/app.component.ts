import { Component } from '@angular/core';
import {CardComponent} from "./shared/card/card.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CardComponent],
  templateUrl: './app.component.html',
  // TODO remove if card-call is moved to individual components
  styleUrl: './app.component.scss'
})
export class AppComponent {
}
