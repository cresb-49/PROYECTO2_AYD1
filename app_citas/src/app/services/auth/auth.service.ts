
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse, HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable } from 'rxjs';

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
  private authStatusSubject = new BehaviorSubject<boolean>(this.isLoggedIn());

  constructor(private router: Router, private httpService: HttpService, private toastr: ToastrService) { }

  get authStatus$(): Observable<boolean> {
    return this.authStatusSubject.asObservable();
  }

  // Método para iniciar sesión
  login(email: string, password: string): boolean {
    const payload = {
      email: email,
      password: password
    };
    this.httpService.post<any>('usuario/public/login', payload).subscribe(
      {
        next: (data: ApiResponse) => {
          this.isAuthenticated = true;
          console.log('response:', data.data);
          this.saveLocalStorage(data.data);
          this.toastr.success( `${this.getFullName()}`,'Bienvenido!');
          this.authStatusSubject.next(true);
          this.router.navigate(['/']);
        },
        error: (error: ApiResponse) => {
          this.isAuthenticated = false;
          console.log('Error:', error);
          this.toastr.error('Error', 'Credenciales inválidas');
          this.clearLocalStorage();
          this.authStatusSubject.next(false);
        }
      }
    );
    return this.isAuthenticated;
  }

  // Método para cerrar sesión
  logout(): void {
    this.isAuthenticated = false;
    this.roles = [];
    this.permissions = [];
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('roles');
    localStorage.removeItem('permissions');
    this.authStatusSubject.next(false);
    this.router.navigate(['/login']);
  }

  // Verificar si el usuario está autenticado
  isLoggedIn(): boolean {
    return this.isAuthenticated || localStorage.getItem('isAuthenticated') === 'true';
  }

  getToken(): string {
    return this.token !== '' ? this.token : localStorage.getItem('token') || '';
  }

  getName(): string {
    return this.name !== '' ? this.name : localStorage.getItem('name') || '';
  }

  getLastname(): string {
    return this.lastname !== '' ? this.lastname : localStorage.getItem('lastname') || '';
  }

  getEmail(): string {
    return this.email !== '' ? this.email : localStorage.getItem('email') || '';
  }

  getFullName(): string {
    return `${this.getName()} ${this.getLastname()}`;
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

  private saveLocalStorage(payload: any = null): void {
    console.log('payload:', payload);
    if (payload) {
      this.token = payload.jwt;
      this.name = payload.usuario.nombres;
      this.lastname = payload.usuario.apellidos;
      this.email = payload.usuario.email;
      this.roles = this.getPayloadRoles(payload.usuario.roles);
      this.permissions = [];
    }
    localStorage.setItem('token', this.token);
    localStorage.setItem('name', this.name);
    localStorage.setItem('lastname', this.lastname);
    localStorage.setItem('email', this.email);
    localStorage.setItem('isAuthenticated', this.isAuthenticated.toString());
    localStorage.setItem('roles', JSON.stringify(this.roles));
    localStorage.setItem('permissions', JSON.stringify(this.permissions));
  }

  private getPayloadRoles(roles: any[]): string[] {
    return roles.map((role: any) => role.rol.nombre);
  }

  private clearLocalStorage(): void {
    this.token = '';
    this.name = '';
    this.lastname = '';
    this.email = '';
    this.isAuthenticated = false;
    this.roles = [];
    this.permissions = [];
    localStorage.removeItem('token');
    localStorage.removeItem('name');
    localStorage.removeItem('lastname');
    localStorage.removeItem('email');
    localStorage.removeItem('roles');
    localStorage.removeItem('permissions');
    localStorage.setItem('isAuthenticated', 'false');
  }

  logOut(): void {
    this.clearLocalStorage();
    this.router.navigate(['/login']);
  }
}
