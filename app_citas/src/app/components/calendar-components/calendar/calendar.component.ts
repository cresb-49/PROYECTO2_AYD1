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
  weekDays: string[] = ["Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"];
  monthNames: string[] = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
  currentDate: Date = new Date();
  currentMonth: number = new Date().getMonth();
  currentYear: number = new Date().getFullYear();
  diasMesActual: DayMonthCalendarComponent[] = [];

  constructor() { }

  ngOnInit() {
    this.obtenerDiasMesActual();
    this.setMothAndYearByDate(this.currentDate);
  }

  setMothAndYearByDate(date: Date): void {
    this.currentMonth = date.getMonth();
    this.currentYear = date.getFullYear();
  }

  obtenerDiasMesActual() {
    // Obtenermos cuantos días tiene el mes actual
    const diasMes = new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0).getDate();
    console.log(diasMes);
    // Dia de la semana en que inicia el mes
    const diaInicioMes = new Date(new Date().getFullYear(), new Date().getMonth(), 1).getDay();
    // Dia de la semana en que termina el mes
    const diaFinMes = new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0).getDay();
    console.log(diaInicioMes);
    console.log(diaFinMes);
    if (!(diaInicioMes === 0)) {
      //Obtenemos los ultimos x días del mes anterior
      const diasMesAnterior = new Date(new Date().getFullYear(), new Date().getMonth(), 0).getDate();
      for (let i = diasMesAnterior - diaInicioMes + 1; i <= diasMesAnterior; i++) {
        this.diasMesActual.push({ numberDay: i, eventsConfig: [], isMonthDay: false, showEvents: i % 2 === 0 });
      }
    }
    for (let i = 1; i <= diasMes; i++) {
      this.diasMesActual.push({ numberDay: i, eventsConfig: [], isMonthDay: true, showEvents: i % 2 === 0 });
    }
    //Si el mes termina en sabado no agregamos más días del siguiente mes
    if (!(diaFinMes === 6)) {
      const dias_agregar = 7 - (diaFinMes + 1);
      for (let i = 1; i <= dias_agregar; i++) {
        this.diasMesActual.push({ numberDay: i, eventsConfig: [], isMonthDay: false, showEvents: i % 2 === 0 });
      }
    }
  }


}
