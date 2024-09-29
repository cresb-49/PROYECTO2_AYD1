import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse, ErrorApiResponse, HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';
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

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

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

  sendRecoveryEmail(correoElectronico: string) {
    //Verificamos que el emial no este vacio
    if (!correoElectronico) {
      this.toastr.error('El correo electrónico no puede estar vacío');
      return;
    }
    return this.httpService.post<any>('usuario/public/mailDeRecupeacion', { correoElectronico }).subscribe(
      {
        next: (data: ApiResponse) => {
          this.toastr.success('Correo enviado, revisa tu bandeja de entrada');
        },
        error: (data: ErrorApiResponse) => {
          this.toastr.error('Correo no enviado, intenta de nuevo');
        }
      }
    );
  }
}
