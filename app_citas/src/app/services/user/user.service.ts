import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse, ErrorApiResponse, HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';
import { Dia } from '../dia/dia.service';
export interface signUpCliente {
  nombres: string;
  apellidos: string;
  email: string;
  password: string;
  phone: string;
  nit: string;
  cui: string;
}

export interface UpdateInfoUser {
  id: number;
  nombres: string;
  apellidos: string;
  email: string;
  phone: string;
  nit: string;
  cui: string;
}

export interface UpdateCreateInfoEmpleado {
  id?: number;
  nombres: string;
  apellidos: string;
  email: string;
  phone: string;
  nit?: string;
  cui: string;
  password?: string;
}

export interface HorarioEmpleado {
  dia: Dia,
  entrada: string,
  salida: string
}

export interface EmpleadoUpdateCreate {
  usuario: UpdateCreateInfoEmpleado,
  rol: Rol,
  horarios: HorarioEmpleado[]
}

export interface UpdateUserPassword {
  id: number;
  password: string;
  newPassword: string;
}

export interface Rol {
  id: number;
  nombre: string;
}

export enum UserRoles {
  CLIENTE = 'CLIENTE',
  ADMIN = 'ADMIN',
  EMPLEADO = 'EMPLEADO'
}

export interface RecoveryPasswordSend {
  nuevaPassword: string;
  codigo: string;
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

  getUsers(): any {
    return this.httpService.get<any>('usuario/private/getUsuarios', null, true);
  }

  getPerfil(id: number | string) {
    return this.httpService.get<any>(`usuario/protected/getPerfil/${id}`, null, true);
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

  sendNewPassword(payload: RecoveryPasswordSend) {
    this.httpService.patch<any>('usuario/public/recuperarPassword', payload).subscribe({
      next: (data: ApiResponse) => {
        this.toastr.success('Contraseña cambiada con éxito');
        this.router.navigate(['/login']);
      },
      error: (data: ErrorApiResponse) => {
        this.toastr.error(data.error, 'Error al cambiar la contraseña');
      }
    });
  }

  changeUserInfo(payload: UpdateInfoUser) {
    return this.httpService.patch<any>('usuario/protected/actualizarUsuarioSinPassword', payload, true);
  }

  changeUserPassword(payload: UpdateUserPassword) {
    return this.httpService.patch<any>('usuario/protected/cambiarPassword', payload, true);
  }

  getRolesGenericos() {
    return this.httpService.get<any>('rol/private/restricted/getRolesGenericos', null, true);
  }

  getEmpleados() {
    return this.httpService.get<any>('empleado/private/empleados', null, true);
  }

  getEmpleado(id: number) {
    return this.httpService.get<any>(`empleado/private/empleado/${id}`, null, true);
  }

  elimiarUsuario(id: number) {
    return this.httpService.delete<any>(`usuario/private/usuario/${id}`, true);
  }

  eliminarEmpleado(id: number) {
    return this.httpService.delete<any>(`empleado/private/empleado/${id}`, true);
  }

  crearEmpleado(payload: any) {
    return this.httpService.post<any>('usuario/private/crearEmpleado', payload, true);
  }

  updateEmpleado(payload: any) {
    return this.httpService.patch<any>('usuario/private/actualizarEmpleado', payload, true);
  }
}
