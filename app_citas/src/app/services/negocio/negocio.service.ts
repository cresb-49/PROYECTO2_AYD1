import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse, ErrorApiResponse, HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';

export interface Dia{
  id:number;
  nombre:string;
}
export interface Horario{
  dia: Dia;
  apertura:string;
  cierre:string;
}
export interface Negocio {
  id: number;
  nombre: string;
  logo: string;
  asignacion_manual: boolean;
  direccion: string;
  horarios: Horario[];
}

export interface PayloadNegocio {
  id: number;
  nombre: string;
  logo: string;
  asignacionManual: boolean;
  direccion: string;
}

export interface InfoNegocio {
  id: number;
  nombre: string;
  logo: string;
}

@Injectable({
  providedIn: 'root'
})
export class NegocioService {

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

  getInfoNegocio() {
    return this.httpService.get<any>('negocio/public/negocio')
  }

  getNegocio() {
    return this.httpService.get<any>('negocio/private/negocio', null, true) //TODO: debe ser privada
  }

  updateNegocio(payload: PayloadNegocio) {
    return this.httpService.patch<any>('negocio/private/negocio', payload, true);
  }

}
