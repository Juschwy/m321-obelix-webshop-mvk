import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenhirDto } from '../models/menhir.dto';
import { CartService } from '../cart/cart.service';

@Component({
  selector: 'app-menhir-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './menhir-card.component.html',
  styleUrl: './menhir-card.component.scss'
})
export class MenhirCardComponent {
  @Input() menhir!: MenhirDto;
  
  private cartService = inject(CartService);

  getPrice(): number {
    // Simple price calculation based on weight and decorativeness
    const basePrice = this.menhir.weight * 1500;
    const decorativenessMultiplier: { [key: string]: number } = {
      'PLAIN': 1.0,
      'SIMPLE': 1.2,
      'DECORATED': 1.5,
      'ORNATE': 2.0,
      'MASTERWORK': 3.0
    };
    const multiplier = decorativenessMultiplier[this.menhir.decorativeness] || 1.0;
    return Math.round(basePrice * multiplier);
  }

  getFormattedPrice(): string {
    return new Intl.NumberFormat('de-CH', { 
      style: 'currency', 
      currency: 'CHF',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(this.getPrice());
  }

  getHeight(): string {
    // Mock height calculation - in real app this would come from backend
    return (this.menhir.weight * 1.14).toFixed(1);
  }

  getWeightInKg(): number {
    return Math.round(this.menhir.weight * 1000);
  }

  onAddToCart(): void {
    this.cartService.addItem(this.menhir);
  }
}
