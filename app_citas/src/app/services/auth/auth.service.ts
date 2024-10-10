
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse, HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable } from 'rxjs';
import * as CryptoJS from 'crypto-js';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private id: number = 0; // ID del usuario
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
          this.toastr.success(`${this.getFullName()}`, 'Bienvenido!');
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
    return this.isAuthenticated || this.getFromLocalStorage('isAuthenticated') === 'true';
  }

  private encrypt(data: string): string {
    return CryptoJS.AES.encrypt(data, environment.encryptionKey).toString();
  }

  private decrypt(data: string): string {
    return CryptoJS.AES.decrypt(data, environment.encryptionKey).toString(CryptoJS.enc.Utf8);
  }

  getId(): number {
    return this.id !== 0 ? this.id : parseInt(this.getFromLocalStorage('id') || '0');
  }

  getToken(): string {
    return this.token !== '' ? this.token : this.getFromLocalStorage('token') || '';
  }

  getName(): string {
    return this.name !== '' ? this.name : this.getFromLocalStorage('name') || '';
  }

  getLastname(): string {
    return this.lastname !== '' ? this.lastname : this.getFromLocalStorage('lastname') || '';
  }

  getEmail(): string {
    return this.email !== '' ? this.email : this.getFromLocalStorage('email') || '';
  }

  getFullName(): string {
    return `${this.getName()} ${this.getLastname()}`;
  }

  // Obtener roles
  getUserRoles(): string[] {
    return this.roles.length > 0 ? this.roles : JSON.parse(this.getFromLocalStorage('roles') || '[]');
  }

  // Obtener permisos
  getUserPermissions(): string[] {
    return this.permissions.length > 0 ? this.permissions : JSON.parse(this.getFromLocalStorage('permissions') || '[]');
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
    if (payload) {
      const rolAndPermissions = this.getPayloadRoles(payload.usuario.roles);
      this.id = payload.usuario.id;
      this.token = payload.jwt;
      this.name = payload.usuario.nombres;
      this.lastname = payload.usuario.apellidos;
      this.email = payload.usuario.email;
      this.roles = rolAndPermissions.roles;
      this.permissions = rolAndPermissions.permissions;
    }
    this.setToLocalStorage('id', this.id.toString());
    this.setToLocalStorage('token', this.token);
    this.setToLocalStorage('name', this.name);
    this.setToLocalStorage('lastname', this.lastname);
    this.setToLocalStorage('email', this.email);
    this.setToLocalStorage('isAuthenticated', this.isAuthenticated.toString());
    this.setToLocalStorage('roles', JSON.stringify(this.roles));
    this.setToLocalStorage('permissions', JSON.stringify(this.permissions));
  }

  private getPayloadRoles(roles: any[]): { roles: string[], permissions: string[] } {
    let rolesUsuario = [];
    for (const rol of roles) {
      rolesUsuario.push(rol.rol.nombre);
    }
    return {
      roles: rolesUsuario,
      permissions: []
    }
  }

  private clearLocalStorage(): void {
    this.token = '';
    this.name = '';
    this.lastname = '';
    this.email = '';
    this.isAuthenticated = false;
    this.roles = [];
    this.permissions = [];
    localStorage.removeItem('id');
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

  private setToLocalStorage(key: string, value: string): void {
    const encryptedValue = this.encrypt(value);
    localStorage.setItem(key, encryptedValue);
  }

  private getFromLocalStorage(key: string): string | null {
    const encryptedValue = localStorage.getItem(key);
    if (encryptedValue) {
      return this.decrypt(encryptedValue);
    }
    return null;
  }
}
