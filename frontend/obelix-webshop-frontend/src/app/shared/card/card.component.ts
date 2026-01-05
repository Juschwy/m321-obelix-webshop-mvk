import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss',
  standalone: true
})
export class CardComponent {
  @Input() title = '';
  @Input() description = '';
  @Input() weight: number | null = null;
  @Input() size = '';
  @Input() prize: number | null = null;
}
