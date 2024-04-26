import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent, HttpResponse,
} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import { SnakeToCamelService } from './snake-to-camel.service';

@Injectable()
export class SnakeToCamelInterceptor implements HttpInterceptor {
  constructor(private snakeToCamelService: SnakeToCamelService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<any> {
    return next.handle(request).pipe(
      map((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          const convertedBody = this.snakeToCamelService.convertToCamelCase(event.body);
          return event.clone({ body: convertedBody });
        }
        return event;
      })
    );
  }
}
