import { Component, inject, OnInit, signal } from '@angular/core';
import { catchError, finalize, map, Observable, of, switchMap } from 'rxjs';
import { CardComponent } from '../shared/card/card.component';
import { CartComponent } from './cart/cart.component';
import { MenhirType } from '../shared/model/menhir-type';
import { MenhirControllerService, MenhirDto } from '../../api';

@Component({
  selector: 'app-webshop',
  imports: [
    CardComponent,
    CartComponent
  ],
  templateUrl: './webshop.component.html',
  styleUrl: './webshop.component.scss',
})
export class WebshopComponent implements OnInit {
  private readonly menhirService = inject(MenhirControllerService);

  readonly menhirs$$ = signal<MenhirViewModel[]>([]);
  readonly isLoading$$ = signal(true);
  readonly error$$ = signal<string | null>(null);

  ngOnInit(): void {
    this.loadMenhirs();
  }

  private loadMenhirs(): void {
    this.isLoading$$.set(true);
    this.error$$.set(null);

    this.menhirService
      .getAllMenhirs('body')
      .pipe(
        switchMap((response) => this.parseResponse<Array<MenhirDto>>(response)),
        map((menhirs) => (menhirs ?? []).map((menhir) => this.toViewModel(menhir))),
        catchError(() => {
          this.error$$.set('Unable to load menhirs right now.');
          return of([]);
        }),
        finalize(() => this.isLoading$$.set(false)),
      )
      .subscribe((menhirs) => this.menhirs$$.set(menhirs));
  }

  private parseResponse<T>(response: unknown): Observable<T> {
    if (response instanceof Blob) {
      return of(response).pipe(
        switchMap((blob) => blob.text()),
        map((text) => (text ? (JSON.parse(text) as T) : ([] as T))),
      );
    }
    return of(response as T);
  }

  private toViewModel(menhir: MenhirDto): MenhirViewModel {
    const decorativeness = this.resolveDecorativeness(menhir.decorativeness);
    const weight = menhir.weight ?? null;
    return {
      id: menhir.id ?? `${decorativeness}-${menhir.stoneType ?? 'menhir'}`,
      title: menhir.stoneType ?? 'Menhir',
      description: menhir.description ?? 'A handcrafted menhir for your collection.',
      weight,
      size: weight === null ? null : Math.round((weight / 1300) * 10) / 10,
      price: weight === null ? null : this.priceFrom(weight, decorativeness),
      menhirType: decorativeness,
    };
  }

  private resolveDecorativeness(value?: MenhirDto['decorativeness']): MenhirType {
    if (!value) return MenhirType.SIMPLE;
    if (Object.values(MenhirType).includes(value as MenhirType)) {
      return value as MenhirType;
    }
    return MenhirType.SIMPLE;
  }

  private priceFrom(weight: number, decorativeness: MenhirType): number {
    const base = Math.round(weight * 0.75);
    const factor: Record<MenhirType, number> = {
      [MenhirType.PLAIN]: 1,
      [MenhirType.SIMPLE]: 1.1,
      [MenhirType.DECORATED]: 1.25,
      [MenhirType.ORNATE]: 1.4,
      [MenhirType.MASTERWORK]: 1.6,
    };
    return Math.max(1, Math.round(base * factor[decorativeness]));
  }
}

interface MenhirViewModel {
  id: string;
  title: string;
  description: string;
  weight: number | null;
  size: number | null;
  price: number | null;
  menhirType: MenhirType;
}
