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

  obtenerReservasCliente(year: number, month: number) {
    return this.httpService.get<any>(`reserva/protected/getCitasDelMes/${year}/${month}`, null, true);
  }

  cancelarReserva(id: number) {
    return this.httpService.patch<any>(`reserva/cliente/cancelarReserva/${id}`, null, true);
  }

  reservaById(id: number) {
    return this.httpService.get<any>(`reserva/private/reserva/${id}`, null, true);
  }

  procesarReserva(id: number) {
    return this.httpService.patch<any>(`reserva/private/restricted/realizarReserva/${id}`, null, true, 'blob');
  }

  getComprobanteReservaByCliente(id: number) {
    return this.httpService.get<any>(`reserva/cliente/comprobanteReservaPorId/${id}`, null, true, 'blob');
  }

  getComprobanteReservaByAdmin(id: number) {
    return this.httpService.get<any>(`reserva/private/restricted/comprobanteReservaPorId/${id}`, null, true, 'blob');
  }

  getFacturaByid(id: number) {
    return this.httpService.get<any>(`factura/cliente/facturaPorId/${id}`, null, true, 'blob');
  }

}
