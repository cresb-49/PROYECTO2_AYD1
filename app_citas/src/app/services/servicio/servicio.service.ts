import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { HttpService } from '../http/http.service';
import { Router } from '@angular/router';
import { Rol } from '../user/user.service';

export interface Servicio {
  id?: number;
  nombre: string;
  duracion: number;
  imagen: string;
  costo: number;
  detalles: string;
  rol:Rol;
}

@Injectable({
  providedIn: 'root'
})
export class ServicioService {

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

  getServicios() {
    return this.httpService.get<any>('servicio/public/servicios')
  }

  getServiciosLikeName(name: string) {
    return this.httpService.get<any>(`servicio/servicios/nombre/${name}`)
  }

  getServicio(id: number) {
    return this.httpService.get<any>(`servicio/public/servicio/${id}`)
  }

  updateServicio(payload: any) {
    return this.httpService.patch<any>('servicio/private/servicio', payload, true);
  }

  deleteServicio(id: number) {
    return this.httpService.delete<any>(`servicio/private/servicio/${id}`, true);
  }

  createServicio(payload: any) {
    return this.httpService.post<any>('servicio/private/servicio', payload, true);
  }

}
