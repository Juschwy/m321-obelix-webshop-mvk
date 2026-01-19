import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {provideHttpClient} from '@angular/common/http';

import {routes} from './app.routes';
import {provideStore} from '@ngrx/store';
import { cartReducer } from '../store/reducer/cart.reducer';
import {provideDefaultClient} from '../api';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(),
    provideDefaultClient({basePath: ''}),
    provideStore({ cart: cartReducer }),
  ],
};
