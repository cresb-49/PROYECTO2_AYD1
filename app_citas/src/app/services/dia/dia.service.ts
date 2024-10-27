import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';

export interface Dia {
  id: number;
  nombre: string;
}

export enum NombresDias {
  LUNES = 'Lunes',
  MARTES = 'Martes',
  MIERCOLES = 'Miércoles',
  JUEVES = 'Jueves',
  VIERNES = 'Viernes',
  SABADO = 'Sábado',
  DOMINGO = 'Domingo'
}

export const ASOCIACION_DIAS_NOMBRE: Map<number, string> = new Map(
  [
    [0, NombresDias.LUNES],
    [1, NombresDias.MARTES],
    [2, NombresDias.MIERCOLES],
    [3, NombresDias.JUEVES],
    [4, NombresDias.VIERNES],
    [5, NombresDias.SABADO],
    [6, NombresDias.DOMINGO]
  ]
);

@Injectable({
  providedIn: 'root'
})
export class DiaService {

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

  getDias() {
    return this.httpService.get<any>('dia/public/dias');
  }

}
