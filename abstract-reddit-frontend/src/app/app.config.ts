import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import { provideRouter } from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import routes from './app.routes';
import {CamelToSnakeInterceptor} from "./converter/camel-to-snake.interceptor";
import {SnakeToCamelInterceptor} from "./converter/snake-to-camel.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    importProvidersFrom(HttpClientModule),
    { provide: HTTP_INTERCEPTORS, useClass: CamelToSnakeInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: SnakeToCamelInterceptor, multi: true }
  ]
};
