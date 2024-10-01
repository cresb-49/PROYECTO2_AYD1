import { Component, HostListener, ElementRef, OnInit, Input, OnDestroy } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../services/theme/theme.service';
import { AuthService } from '../../services/auth/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';

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

  navOptions = [
    { name: 'Inicio', path: '/' },
    { name: 'Canchas', path: '/canchas' },
    { name: 'Calendario', path: '/calendar' },
  ];

  themeIcon = 'wb_sunny';
  isLoggedIn = false;
  private authSubscription: Subscription = new Subscription();

  constructor(
    private elementRef: ElementRef,
    public themeService: ThemeService,
    private authService: AuthService
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
}
