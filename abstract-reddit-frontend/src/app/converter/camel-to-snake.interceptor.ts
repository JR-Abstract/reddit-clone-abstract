import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { CamelToSnakeService } from './camel-to-snake.service';

@Injectable()
export class CamelToSnakeInterceptor implements HttpInterceptor {
  constructor(private camelToSnakeService: CamelToSnakeService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable <any> {
  const convertedBody = this.camelToSnakeService.convertToSnakeCase(request.body);
  const modifiedRequest = request.clone({ body: convertedBody });
  return next.handle(modifiedRequest);
}
}
