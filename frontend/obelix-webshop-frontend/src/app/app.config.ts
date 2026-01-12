import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {provideHttpClient} from '@angular/common/http';

import {routes} from './app.routes';
import {provideDefaultClient} from '../api';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(),
    provideDefaultClient({basePath: `${location.protocol}//${location.hostname}:8080`}),
  ],
};
