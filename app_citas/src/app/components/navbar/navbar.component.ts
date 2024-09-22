import { Component, HostListener, ElementRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../services/theme.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone: true,
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  imports: [CommonModule,MatIconModule]
})
export class NavbarComponent implements OnInit {

  constructor(private elementRef: ElementRef,public themeService: ThemeService) {}
  themeIcon = 'wb_sunny';

  ngOnInit() {
    this.themeService.setTheme(this.themeService.currentTheme);
    this.setThemeIcon();
  }

  // Cambiar el tema
  toggleTheme() {
    this.themeService.toggleTheme();
    this.setThemeIcon();
  }

  // Establecer el icono según el tema
  setThemeIcon() {
    this.themeIcon = this.themeService.currentTheme === 'light' ? 'wb_sunny' : 'nightlight_round';
  }

  userMenuOpen = false;
  navbarOpen = false;

  toggleUserMenu() {
    this.userMenuOpen = !this.userMenuOpen;
  }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

  // Escuchar los eventos de clic en el documento
  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    // Verificar si el clic ocurrió fuera del menú del usuario
    if (
      this.userMenuOpen &&
      !this.elementRef.nativeElement.querySelector('#user-dropdown').contains(event.target)
    ) {
      this.userMenuOpen = false;
    }

    // Verificar si el clic ocurrió fuera del menú de navegación
    if (
      this.navbarOpen &&
      !this.elementRef.nativeElement.querySelector('#navbar-user').contains(event.target) &&
      !this.elementRef.nativeElement.querySelector('.inline-flex').contains(event.target)
    ) {
      this.navbarOpen = false;
    }
  }
}
