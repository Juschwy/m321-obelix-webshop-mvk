import { CommonModule } from '@angular/common';
import { Component, DestroyRef, Inject, input, output, signal } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss',
})
export class CheckoutComponent {
  private readonly router = Inject(Router);
  private readonly destroyRef = Inject(DestroyRef);

  // Inputs
  readonly isOpen = input.required<boolean>();
  readonly total = input.required<number>();

  // Outputs
  readonly closed = output<void>();
  readonly purchased = output<void>(); // optional (z.B. Cart leeren)

  // Local UI state
  readonly isBuying$$ = signal(false);
  readonly isSuccess$$ = signal(false);

  private redirectTimer: ReturnType<typeof setTimeout> | null = null;

  close(): void {
    if (this.isBuying$$()) return; // optional: waehrend Kauf blockieren
    this.clearTimer();
    this.resetState();
    this.closed.emit();
  }

  buy(): void {
    if (this.isBuying$$() || this.isSuccess$$()) return;

    this.isBuying$$.set(true);

    // Hier wuerdest du spaeter z.B. API call machen.
    // Demo: sofort "success".
    this.isBuying$$.set(false);
    this.isSuccess$$.set(true);
    this.purchased.emit();

    this.clearTimer();
    this.redirectTimer = setTimeout(() => {
      this.resetState();
      this.closed.emit();           // Modal schliessen
      void this.router.navigateByUrl('/'); // zur Main Seite
    }, 5000);

    // Cleanup falls Komponente vorher unmounted wird
    this.destroyRef.onDestroy(() => this.clearTimer());
  }

  private resetState(): void {
    this.isBuying$$.set(false);
    this.isSuccess$$.set(false);
  }

  private clearTimer(): void {
    if (this.redirectTimer) {
      clearTimeout(this.redirectTimer);
      this.redirectTimer = null;
    }
  }
}
