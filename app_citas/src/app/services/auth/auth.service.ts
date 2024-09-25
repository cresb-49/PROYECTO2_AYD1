import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private token: string = ''; // Token de autenticación
  private name: string = ''; // Nombre del usuario
  private lastname: string = ''; // Apellido del usuario
  private email: string = ''; // Correo electrónico del usuario
  private isAuthenticated = false; // Estado de autenticación
  private roles: string[] = []; // Roles del usuario
  private permissions: string[] = []; // Permisos del usuario

  constructor(private router: Router) { }

  // Método para iniciar sesión
  login(email: string, password: string): boolean {
    // Lógica para validar el inicio de sesión
    if (email === 'admin@example.com' && password === 'password') {
      this.isAuthenticated = true;
      this.roles = ['ADMIN'];
      this.permissions = ['CREATE', 'READ', 'UPDATE', 'DELETE'];
      localStorage.setItem('isAuthenticated', 'true');
      localStorage.setItem('roles', JSON.stringify(this.roles));
      localStorage.setItem('permissions', JSON.stringify(this.permissions));
      return true;
    } else if (email === 'user@example.com' && password === 'password') {
      this.isAuthenticated = true;
      this.roles = ['USER'];
      this.permissions = ['READ'];
      localStorage.setItem('isAuthenticated', 'true');
      localStorage.setItem('roles', JSON.stringify(this.roles));
      localStorage.setItem('permissions', JSON.stringify(this.permissions));
      return true;
    }
    return false;
  }

  // Método para cerrar sesión
  logout(): void {
    this.isAuthenticated = false;
    this.roles = [];
    this.permissions = [];
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('roles');
    localStorage.removeItem('permissions');
    this.router.navigate(['/login']);
  }

  // Verificar si el usuario está autenticado
  isLoggedIn(): boolean {
    return this.isAuthenticated || localStorage.getItem('isAuthenticated') === 'true';
  }

  getToken(): string {
    return this.token;
  }

  getName(): string {
    return this.name;
  }

  getLastname(): string {
    return this.lastname;
  }

  getEmail(): string {
    return this.email;
  }

  getFullName(): string {
    return `${this.name} ${this.lastname}`;
  }

  // Obtener roles
  getUserRoles(): string[] {
    return this.roles.length > 0 ? this.roles : JSON.parse(localStorage.getItem('roles') || '[]');
  }

  // Obtener permisos
  getUserPermissions(): string[] {
    return this.permissions.length > 0 ? this.permissions : JSON.parse(localStorage.getItem('permissions') || '[]');
  }

  // Verificar si el usuario tiene un rol específico
  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }

  // Verificar si el usuario tiene un permiso específico
  hasPermission(permission: string): boolean {
    return this.getUserPermissions().includes(permission);
  }

  logOut(): void {
    this.isAuthenticated = false;
    this.roles = [];
    this.permissions = [];
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('roles');
    localStorage.removeItem('permissions');
    this.router.navigate(['/login']);
  }
}
