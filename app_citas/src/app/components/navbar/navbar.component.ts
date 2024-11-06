import { Component, HostListener, ElementRef, OnInit, Input, OnDestroy } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../services/theme/theme.service';
import { AuthService } from '../../services/auth/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';
import { InfoNegocio, NegocioService } from '../../services/negocio/negocio.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { NativeUserRoles } from '../../services/auth/types';

@Component({
  standalone: true,
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  imports: [CommonModule, RouterModule, MatIconModule],
})
export class NavbarComponent implements OnInit, OnDestroy {
  @Input() emptyNav = false;
  name = '';
  email = '';

  infoNegocio: InfoNegocio = {
    id: 0,
    nombre: 'Negocio',
    logo: ''
  }

  isLoggedIn = false;

  navOptions = [
    { name: 'Inicio', path: '/'},
    // { name: 'Canchas', path: '/canchas',},
    { name: 'Calendario', path: '/calendar'},
  ];

  themeIcon = 'wb_sunny';
  private authSubscription: Subscription = new Subscription();

  constructor(
    private elementRef: ElementRef,
    public themeService: ThemeService,
    private authService: AuthService,
    private negocioService: NegocioService
  ) { }

  ngOnInit() {
    this.themeService.setTheme(this.themeService.currentTheme);
    this.setThemeIcon();
    // Subscribe to auth status
    this.authSubscription = this.authService.authStatus$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      this.name = this.authService.getFullName();
      this.email = this.authService.getEmail();
    });
    this.obtenerDatosNegocio();
  }

  obtenerDatosNegocio() {
    this.negocioService.getInfoNegocio().subscribe(
      {
        next: (response: ApiResponse) => {
          this.infoNegocio = {
            id: response.data.id,
            nombre: response.data.nombre,
            logo: response.data.logo
          }
        }
      }
    )
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  logout() {
    this.authService.logout();
  }

  toggleTheme() {
    this.themeService.toggleTheme();
    this.setThemeIcon();
  }

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

  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    if (
      this.userMenuOpen &&
      !this.elementRef.nativeElement.querySelector('#user-dropdown').contains(event.target)
    ) {
      this.userMenuOpen = false;
    }

    if (
      this.navbarOpen &&
      !this.elementRef.nativeElement.querySelector('#navbar-user').contains(event.target) &&
      !this.elementRef.nativeElement.querySelector('.inline-flex').contains(event.target)
    ) {
      this.navbarOpen = false;
    }
  }

  isCliente() {
    return this.authService.hasRole(NativeUserRoles.CLIENTE);
  }
}
