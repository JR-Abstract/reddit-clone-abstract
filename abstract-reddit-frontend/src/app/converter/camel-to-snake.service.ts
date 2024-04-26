import { Injectable } from '@angular/core';

interface CamelCaseObject {
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class CamelToSnakeService {

  constructor() { }

  convertToSnakeCase(data: CamelCaseObject): any {
    if (typeof data !== 'object' || data === null) {
      return data;
    }
    if (Array.isArray(data)) {
      return data.map(item => this.convertToSnakeCase(item));
    }

    const result: CamelCaseObject = {};
    return Object.keys(data).reduce((result, key) => {
      const value = data[key];
      const snakeCaseKey = key.replace(/[A-Z]/g, (match) => `_${match.toLowerCase()}`);
      result[snakeCaseKey] = this.convertToSnakeCase(value);
      return result;
    }, result);
  }
}
