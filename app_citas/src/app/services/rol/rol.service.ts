import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpService } from '../http/http.service';


export interface Permiso {
  id: number;
  nombre: string;
  ruta?: string;
}
export interface UpdateRole {
  idRol: number;
  permisos: Permiso[];
}

export interface CreateRole {
  rol: Rol;
  permisos: Permiso[];
}

export interface Rol {
  id?: number;
  nombre: string;
}

@Injectable({
  providedIn: 'root'
})
export class RolService {

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

  getRolById(id: number) {
    return this.httpService.get<any>(`rol/private/no_restricted/getRolById/${id}`, null, true);
  }

  createRol(payload: CreateRole) {
    return this.httpService.post<any>('rol/private/restricted/crearRol', payload, true);
  }

  updateRol(payload: Rol) {
    return this.httpService.patch<any>('rol/private/restricted/actualizarRol', payload, true);
  }

  updatePermisosRole(payload: UpdateRole) {
    return this.httpService.patch<any>('rol/private/restricted/actualizarPermisosRol', payload, true);
  }

  getPermisos() {
    return this.httpService.get<any>('permiso/private/restricted/permisos', null, true);
  }


}
