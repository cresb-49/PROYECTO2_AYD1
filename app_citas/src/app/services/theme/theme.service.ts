// theme.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private themeSubject = new BehaviorSubject<string>('light');
  theme$ = this.themeSubject.asObservable();

  constructor() {
    // Comprobar si hay un tema guardado en localStorage
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
      this.themeSubject.next(savedTheme);
      this.setTheme(savedTheme);
    }
  }

  // Cambiar el tema y guardar en localStorage
  toggleTheme() {
    const newTheme = this.themeSubject.value === 'light' ? 'dark' : 'light';
    this.themeSubject.next(newTheme);
    this.setTheme(newTheme);
    localStorage.setItem('theme', newTheme);
  }

  // Aplicar el tema al body
  public setTheme(theme: string) {
    if (theme === 'dark') {
      document.documentElement.classList.add('dark'); // Añadir la clase 'dark' al <html>
    } else {
      document.documentElement.classList.remove('dark');
    }
  }

  // Obtener el tema actual
  get currentTheme() {
    return this.themeSubject.value;
  }
}
