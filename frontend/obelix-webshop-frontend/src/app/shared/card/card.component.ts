import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  ViewChild,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { MenhirType } from '../model/menhir-type';
import { CartItem } from '../../webshop/cart/cart.component';
import * as fromCartActions from '../../../store/action/cart.action';
import { BasketControllerService } from '../../../api';
import { CartUiService } from '../../webshop/cart/cart-ui.service';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss',
  standalone: true,
})
export class CardComponent implements AfterViewInit, OnDestroy {
  private readonly store$ = inject(Store);
  private readonly basketService = inject(BasketControllerService);
  private readonly cartUi = inject(CartUiService);
  private resizeObserver?: ResizeObserver;
  private rafId: number | null = null;

  id$$ = input('');
  title$$ = input('');
  description$$ = input('');
  weight$$ = input<number | null>(null);
  size$$ = input<number | null>(null);
  prize$$ = input<number | null>(null);
  menhirType$$ = input<MenhirType>(MenhirType.SIMPLE);
  readonly isDescriptionTruncated$$ = signal(false);

  @ViewChild('descText', { static: true })
  private readonly descText?: ElementRef<HTMLElement>;

  constructor() {
    effect(() => {
      this.description$$();
      this.queueTruncationCheck();
    });
  }

  ngAfterViewInit(): void {
    this.setupResizeObserver();
    this.queueTruncationCheck();
  }

  ngOnDestroy(): void {
    if (this.rafId !== null) {
      cancelAnimationFrame(this.rafId);
    }
    this.resizeObserver?.disconnect();
  }

  addToCard() {
    const id = this.id$$() || this.title$$();
    const menhir: CartItem = {
      id,
      name: this.title$$(),
      description: this.description$$(),
      price: this.prize$$() ?? 0,
      quantity: 1,
      imageUrl: `assets/images/Menhir_${this.menhirType$$()}.png`,
    };
    this.store$.dispatch(fromCartActions.addItemToCart({ menhirs: [menhir] }));
    this.cartUi.open();
    this.basketService
      .offer({ name: id, count: menhir.quantity })
      .subscribe();
  }

  private queueTruncationCheck(): void {
    if (this.rafId !== null) {
      cancelAnimationFrame(this.rafId);
    }

    this.rafId = requestAnimationFrame(() => {
      this.rafId = null;
      this.updateTruncationState();
    });
  }

  private setupResizeObserver(): void {
    if (!this.descText?.nativeElement || typeof ResizeObserver === 'undefined') {
      return;
    }

    this.resizeObserver = new ResizeObserver(() => this.updateTruncationState());
    this.resizeObserver.observe(this.descText.nativeElement);
  }

  private updateTruncationState(): void {
    const element = this.descText?.nativeElement;
    if (!element) {
      return;
    }

    const isTruncated =
      element.scrollHeight > element.clientHeight + 1 ||
      element.scrollWidth > element.clientWidth + 1;
    this.isDescriptionTruncated$$.set(isTruncated);
  }
}
