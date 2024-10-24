import { Injectable } from '@angular/core';
import { HttpService } from '../http/http.service';

export interface Reserva {
  horaInicio: string;
  horaFin?: string;
  fechaReservacion: string;
}

export interface ReservaCancha extends Reserva {
  canchaId: number
}

export interface CitaServicio extends Reserva {
  empleadoId: number;
  servicioId: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  constructor(
    private httpService: HttpService
  ) { }

  reservarServicio(payload: any, responseType: 'json' | 'blob' = 'json') {
    return this.httpService.post<any>('servicio/cliente/reservaServicio', payload, true, responseType);
  }

  reservarChancha(payload: any, responseType: 'json' | 'blob' = 'json') {
    return this.httpService.post<any>('cancha/cliente/reservarCancha', payload, true, responseType);
  }

}
