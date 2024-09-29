import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpService } from '../http/http.service';

export interface signUpCliente {
  nombres: string;
  apellidos: string;
  email: string;
  password: string;
  phone: string;
  nit: string;
  cui: string;
}

export enum UserRoles {
  CLIENTE = 'CLIENTE',
  ADMIN = 'ADMIN',
  EMPLEADO = 'EMPLEADO'
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private router: Router, private httpService: HttpService) { }

  signUpCliente(payload: signUpCliente) {
    return this.httpService.post<any>('usuario/public/crearCliente', payload);
  }

  getUser(id: number | string) {
    return this.httpService.get<any>(`private/getUsuario/${id}`, null, true);
  }

  getUsers() {
    return this.httpService.get<any>('private/getUsuarios', null, true);
  }

  getPerfil(id: number | string) {
    return this.httpService.get<any>(`protected/getPerfil/${id}`, null, true);
  }
}
