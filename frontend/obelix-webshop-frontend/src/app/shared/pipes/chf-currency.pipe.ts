import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'chfCurrency',
  standalone: true
})
export class ChfCurrencyPipe implements PipeTransform {
  public transform(value: number | null | undefined, fractionDigits: number = 0): string {
    if (value === null || value === undefined) {
      return '';
    }

    return new Intl.NumberFormat('de-CH', {
      style: 'currency',
      currency: 'CHF',
      minimumFractionDigits: fractionDigits,
      maximumFractionDigits: fractionDigits
    }).format(value);
  }
}

