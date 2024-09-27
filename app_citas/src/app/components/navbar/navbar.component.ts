import { Component, HostListener, ElementRef, OnInit, Input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../services/theme/theme.service';
import { AuthService } from '../../services/auth/auth.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone: true,
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  imports: [CommonModule, MatIconModule, RouterModule],
})
export class NavbarComponent implements OnInit {
  @Input() emptyNav = false;

  navOptions= [
    { name: 'Inicio', path: '/' },
    { name: 'Canchas', path: '/canchas' },
    { name: 'Calendario', path: '/calendar' },
  ]

  constructor(
    private elementRef: ElementRef,
    public themeService: ThemeService,
    private authService: AuthService
  ) { }
  themeIcon = 'wb_sunny';
  isLoggedIn = false;

  ngOnInit() {
    // Cargamos el tema actual y asignamos el icono en la vista
    this.themeService.setTheme(this.themeService.currentTheme);
    this.setThemeIcon();
    // Verificamos si el usuario está autenticado
    this.isLoggedIn = this.authService.isLoggedIn();
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
