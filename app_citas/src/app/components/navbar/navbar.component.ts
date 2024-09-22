import { Component, HostListener, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  imports: [CommonModule]
})
export class NavbarComponent {
  userMenuOpen = false;
  navbarOpen = false;

  constructor(private elementRef: ElementRef) {}

  ngOnInit() {}

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
