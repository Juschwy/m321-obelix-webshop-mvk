import { Component, input, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenhirDto } from '../models/menhir.dto';
import { CartService } from '../services/cart.service';
import { ChfCurrencyPipe } from '../shared/pipes/chf-currency.pipe';

@Component({
  selector: 'app-menhir-card',
  standalone: true,
  imports: [CommonModule, RouterModule, ChfCurrencyPipe],
  templateUrl: './menhir-card.component.html',
  styleUrl: './menhir-card.component.scss'
})
export class MenhirCardComponent {
  readonly menhir = input.required<MenhirDto>();
  
  private readonly cartService = inject(CartService);

  protected readonly price$$ = computed(() => {
    const basePrice = this.menhir().weight * 1500;
    const decorativenessMultiplier: { [key: string]: number } = {
      'PLAIN': 1.0,
      'SIMPLE': 1.2,
      'DECORATED': 1.5,
      'ORNATE': 2.0,
      'MASTERWORK': 3.0
    };
    const multiplier = decorativenessMultiplier[this.menhir().decorativeness] || 1.0;
    return Math.round(basePrice * multiplier);
  });

  protected readonly height$$ = computed(() => {
    return (this.menhir().weight * 1.14).toFixed(1);
  });

  protected readonly weightInKg$$ = computed(() => {
    return Math.round(this.menhir().weight * 1000);
  });

  protected onAddToCart(): void {
    this.cartService.addItem(this.menhir());
  }
}
