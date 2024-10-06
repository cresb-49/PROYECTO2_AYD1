import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventConfig, MonthDayCalendarComponent } from "../month-day-calendar/month-day-calendar.component";
import { EventCalendarComponent } from "../event-calendar/event-calendar.component";

export interface DayMonthCalendarComponent {
  numberDay: number,
  eventsConfig: EventConfig[],
  isMonthDay: boolean,
  showEvents: boolean
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
  currentMonth: number = this.currentDate.getMonth();
  currentYear: number = this.currentDate.getFullYear();
  diasMesActual: DayMonthCalendarComponent[] = [];

  constructor() { }

  ngOnInit() {
    this.setMothAndYearByDate(this.currentDate);
    this.obtenerDiasMesActual();
  }

  setMothAndYearByDate(date: Date): void {
    this.currentMonth = date.getMonth();
    this.currentYear = date.getFullYear();
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
    this.obtenerDiasMesActual();
  }

  goToCurrentMonth() {
    this.currentDate = new Date(); // Reiniciamos a la fecha actual
    this.setMothAndYearByDate(this.currentDate); // Actualizamos currentMonth y currentYear
    this.obtenerDiasMesActual(); // Actualizamos los días del mes actual
  }
}
