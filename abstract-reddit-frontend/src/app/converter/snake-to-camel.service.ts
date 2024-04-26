import { Injectable } from '@angular/core';

interface SnakeCaseObject {
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class SnakeToCamelService {

  constructor() { }

  // Метод для перетворення об'єкта з snake_case у camelCase
  convertToCamelCase(data: SnakeCaseObject): any {
    if (typeof data !== 'object' || data === null) {
      return data;
    }
    if (Array.isArray(data)) {
      return data.map(item => this.convertToCamelCase(item));
    }
    // Визначення типу для об'єкта result
    const result: SnakeCaseObject = {};
    return Object.keys(data).reduce((result, key) => {
      const value = data[key];
      const camelCaseKey = key.replace(/_([a-z])/g, (match, group1) => group1.toUpperCase());
      result[camelCaseKey] = this.convertToCamelCase(value);
      return result;
    }, result);
  }
}
