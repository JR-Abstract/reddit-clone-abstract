import { Injectable } from '@angular/core';

interface CamelCaseObject {
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class CamelToSnakeService {

  constructor() { }

  // Метод для перетворення об'єкта з camelCase у snake_case
  convertToSnakeCase(data: CamelCaseObject): any {
    if (typeof data !== 'object' || data === null) {
      return data;
    }
    if (Array.isArray(data)) {
      return data.map(item => this.convertToSnakeCase(item));
    }
    // Визначення типу для об'єкта result
    const result: CamelCaseObject = {};
    return Object.keys(data).reduce((result, key) => {
      const value = data[key];
      const snakeCaseKey = key.replace(/[A-Z]/g, (match) => `_${match.toLowerCase()}`);
      result[snakeCaseKey] = this.convertToSnakeCase(value);
      return result;
    }, result);
  }
}
