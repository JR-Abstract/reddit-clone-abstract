import { Injectable } from '@angular/core';

interface SnakeCaseObject {
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class SnakeToCamelService {

  constructor() { }

  convertToCamelCase(data: SnakeCaseObject): any {
    if (typeof data !== 'object' || data === null) {
      return data;
    }
    if (Array.isArray(data)) {
      return data.map(item => this.convertToCamelCase(item));
    }
    const result: SnakeCaseObject = {};
    return Object.keys(data).reduce((result, key) => {
      const value = data[key];
      const camelCaseKey = key.replace(/_([a-z])/g, (match, group1) => group1.toUpperCase());
      result[camelCaseKey] = this.convertToCamelCase(value);
      return result;
    }, result);
  }
}
