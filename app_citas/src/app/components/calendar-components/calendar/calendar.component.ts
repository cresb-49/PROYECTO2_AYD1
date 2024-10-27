import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventConfig, MonthDayCalendarComponent } from "../month-day-calendar/month-day-calendar.component";
import { EventCalendarComponent } from "../event-calendar/event-calendar.component";
import { ReservaService } from '../../../services/reserva/reserva.service';
import { BehaviorSubject, debounce, debounceTime } from 'rxjs';
import { ApiResponse, ErrorApiResponse } from '../../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';

export interface DayMonthCalendarComponent {
  numberDay: number,
  eventsConfig: EventConfig[],
  isMonthDay: boolean,
  showEvents: boolean
}

export interface ReservaResponse {
  id: number;
  reservador: {
    id: number;
    nombres: string;
    apellidos: string;
    nit?: string | null;
  },
  horaInicio: string;
  horaFin: string;
  fechaReservacion: string;
  idFactura: number | null;
  realizada: boolean;
  canceledAt: string | null;
  adelanto: number;
  totalACobrar: number;
  reservaCancha: {
    id: number;
    cancha: {
      id: number;
      descripcion: string;
    };
  } | null;
  reservaServicio: {
    id: number;
    servicio: {
      id: number;
      nombre: string;
      detalles: string;
    };
  } | null;
}

@Component({
  standalone: true,
  imports: [CommonModule, MonthDayCalendarComponent, EventCalendarComponent],
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {
  weekDays: string[] = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];
  monthNames: string[] = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
  currentDate: Date = new Date();
  currentDay: number = this.currentDate.getDate();
  currentMonth: number = this.currentDate.getMonth();
  currentYear: number = this.currentDate.getFullYear();
  diasMesActual: DayMonthCalendarComponent[] = [];

  reservasMap: Map<string, ReservaResponse[]> = new Map();

  eventosMostrar: ReservaResponse[] = [];

  diaSeleccionado: string | number | null = null;

  private currentDateSubject = new BehaviorSubject<{ currentYear: number, currentMonth: number }>({
    currentYear: this.currentYear,
    currentMonth: this.currentMonth
  });

  constructor(
    private reservaService: ReservaService,
    private toastrService: ToastrService
  ) { }

  async ngOnInit() {
    await this.currentDateSubject.pipe(
      debounceTime(300)
    ).subscribe(async (newCurrentDate: any) => {
      await this.cambioFechaData();
    });
    this.setMothAndYearByDate(this.currentDate);
    this.obtenerDiasMesActual();
    await this.cambioFechaData();
    this.handleDaySend(this.currentDay)
  }

  setMothAndYearByDate(date: Date): void {
    this.currentMonth = date.getMonth();
    this.currentYear = date.getFullYear();
  }

  cambioFechaData(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.reservaService.obtenerReservasCliente(this.currentYear, (this.currentMonth + 1)).subscribe(
        {
          next: (response: ApiResponse) => {
            const data = response.data;
            const citasReservas = this.parseDataCitasReservas(data);
            this.mapearDia(citasReservas);
            this.toastrService.success(response.message, 'Citas/Reservas recuperadas con exito!!!')
            console.log(this.reservasMap);
            resolve(data)
          },
          error: (error: ErrorApiResponse) => {
            this.toastrService.error(error.error, 'Error al obtener la citas/reservaciones del usuario');
            reject(error)
          }
        }
      );
    });
  }

  private parseDataCitasReservas(data: any[] | null): ReservaResponse[] {
    let citasReservas: ReservaResponse[] = [];
    (data ?? []).forEach((value: any) => {
      citasReservas.push({
        id: value.id,
        reservador: {
          id: value.reservador.id,
          nombres: value.reservador.nombres,
          apellidos: value.reservador.apellidos
        },
        horaInicio: value.horaInicio,
        horaFin: value.horaFin,
        fechaReservacion: value.fechaReservacion,
        idFactura: value.idFactura,
        realizada: value.realizada,
        canceledAt: value.canceledAt,
        adelanto: value.adelanto,
        totalACobrar: value.totalACobrar,
        reservaCancha: value.reservaCancha ? {
          id: value.reservaCancha.id,
          cancha: {
            id: value.reservaCancha.cancha.id,
            descripcion: value.reservaCancha.cancha.descripcion
          }
        } : null,
        reservaServicio: value.reservaServicio ? {
          id: value.reservaServicio.id,
          servicio: {
            id: value.reservaServicio.servicio.id,
            nombre: value.reservaServicio.servicio.nombre,
            detalles: value.reservaServicio.servicio.detalles,
          }
        } : null
      })
    });
    return citasReservas;
  }

  private mapearDia(reservas: ReservaResponse[]) {
    //Descomponemos la fecha de reserva para obtener el numero de dia
    let reservasMap: Map<string, ReservaResponse[]> = new Map();
    reservas.map((value: ReservaResponse) => {
      const date_conf = value.fechaReservacion.split('-');
      let current = reservasMap.get(date_conf[2]);
      if (current) {
        current.push(value);
      } else {
        reservasMap.set(date_conf[2], [value])
      }
    })
    this.reservasMap = reservasMap;
  }

  obtenerDiasMesActual() {
    this.diasMesActual = []; // Reiniciamos la lista de días del mes

    // Obtenermos cuantos días tiene el mes actual
    const diasMes = new Date(this.currentYear, this.currentMonth + 1, 0).getDate();
    // Dia de la semana en que inicia el mes
    const diaInicioMes = new Date(this.currentYear, this.currentMonth, 1).getDay();
    // Dia de la semana en que termina el mes
    const diaFinMes = new Date(this.currentYear, this.currentMonth + 1, 0).getDay();

    // Añadir días del mes anterior si el mes no inicia en domingo
    if (!(diaInicioMes === 0)) {
      const diasMesAnterior = new Date(this.currentYear, this.currentMonth, 0).getDate();
      for (let i = diasMesAnterior - diaInicioMes + 1; i <= diasMesAnterior; i++) {
        this.diasMesActual.push({ numberDay: i, eventsConfig: [], isMonthDay: false, showEvents: false });
      }
    }

    // Añadir días del mes actual
    for (let i = 1; i <= diasMes; i++) {
      this.diasMesActual.push({ numberDay: i, eventsConfig: [], isMonthDay: true, showEvents: i % 2 === 0 });
    }

    // Añadir días del siguiente mes si el mes no termina en sábado
    if (!(diaFinMes === 6)) {
      const diasAgregar = 6 - diaFinMes;
      for (let i = 1; i <= diasAgregar; i++) {
        this.diasMesActual.push({ numberDay: i, eventsConfig: [], isMonthDay: false, showEvents: false });
      }
    }
  }

  previousMonth() {
    // Decrementar el mes actual
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }
    this.currentDate = new Date(this.currentYear, this.currentMonth, 1);
    this.currentDateSubject.next({
      currentMonth: this.currentMonth,
      currentYear: this.currentYear
    })
    this.obtenerDiasMesActual();
  }

  nextMonth() {
    // Incrementar el mes actual
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }
    this.currentDate = new Date(this.currentYear, this.currentMonth, 1);
    this.currentDateSubject.next({
      currentMonth: this.currentMonth,
      currentYear: this.currentYear
    })
    this.obtenerDiasMesActual();
  }

  goToCurrentMonth() {
    this.currentDate = new Date(); // Reiniciamos a la fecha actual
    this.currentDateSubject.next({
      currentMonth: this.currentMonth,
      currentYear: this.currentYear
    })
    this.setMothAndYearByDate(this.currentDate); // Actualizamos currentMonth y currentYear
    this.obtenerDiasMesActual(); // Actualizamos los días del mes actual
    this.diaSeleccionado = this.currentDay;
    this.handleDaySend(this.currentDay);
  }

  handleDaySend(day: number) {
    console.log('Dia seleccionado: ', day);
    this.diaSeleccionado = day
    const day_s = day < 10 ? `0${day}` : `${day}`;
    console.log(day_s);

    const eventos = this.reservasMap.get(day_s) ?? [];
    this.eventosMostrar = eventos;
  }

  calcularEventosDelDia(day: number): { reservas: number, citas: number } {
    const day_s = day < 10 ? `0${day}` : `${day}`;
    let reservas = 0;
    let citas = 0;
    const eventos = this.reservasMap.get(day_s) ?? [];
    eventos.map((value: ReservaResponse) => {
      if (value.reservaCancha) {
        reservas++;
      } else if (value.reservaServicio) {
        citas++;
      }
    })
    return { reservas, citas }
  }
}
