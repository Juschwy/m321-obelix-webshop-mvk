import {Component, input} from '@angular/core';
import { MenhirType } from '../model/menhir-type';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss',
  standalone: true
})
export class CardComponent {
  title$$ = input('');
  description$$ = input('');
  weight$$ = input<number | null>(null);
  size$$ = input<number | null>(null);
  prize$$ = input<number | null>(null);
  menhirType$$ = input<MenhirType>(MenhirType.SIMPLE);
}
