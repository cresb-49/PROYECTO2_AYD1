import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse, ErrorApiResponse, HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';
import { Dia } from '../dia/dia.service';
import { DayConfig } from '../../components/schedule-conf/schedule-conf.component';

export interface Horario {
  dia: Dia;
  apertura: string;
  cierre: string;
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
  negocio: {
    id: number;
    nombre: string;
    logo: string;
    asignacionManual: boolean;
    direccion: string;
    porcentajeAnticipo: number;
  };
  horarios: Horario[];
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
    return this.httpService.get<any>('negocio/private/negocio', null, true)
  }

  updateNegocio(payload: PayloadNegocio) {
    return this.httpService.patch<any>('negocio/private/negocio', payload, true);
  }

  obtenerHorariosNegocio(): Promise<DayConfig[]> {
    return new Promise((resolve, reject) => {
      this.getInfoNegocio().subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          const result = [...this.calcularHorario(data.horarios)]
          resolve(result)
        },
        error: (error: ErrorApiResponse) => {
          reject(error)
        }
      })
    })
  }

  public calcularHorario(horarios: any): DayConfig[] {
    let negocioHas: DayConfig[] = horarios.map((horario: any) => {
      return {
        id: horario.dia.id,
        day: horario.dia.nombre,
        init: horario.apertura,
        end: horario.cierre,
        active: true
      } as DayConfig
    })
    return negocioHas;
  }
}
