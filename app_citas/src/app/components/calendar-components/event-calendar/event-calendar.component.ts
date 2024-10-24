import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-event-calendar',
  templateUrl: './event-calendar.component.html',
  styleUrls: ['./event-calendar.component.css']
})
export class EventCalendarComponent implements OnInit {
  mes: string = 'Enero';
  dia: string | number = 10;
  year: string | number = 2020;

  @Input() fecha: string = '2020-01-10';
  @Input() init: string = '10:00';
  @Input() fin: string = '11:00';
  @Input() titulo: string = 'Meeting with a friend';
  @Input() descripcion: string = 'Meet-Up for Travel Destination Discussion';

  // Lista de meses en español
  private meses: string[] = [
    'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
  ];

  constructor() { }

  ngOnInit() {
    this.desgloseFecha(this.fecha);
  }

  desgloseFecha(fechaStr: string) {
    const fechaPartes = fechaStr.split('-');

    if (fechaPartes.length === 3) {
      const [anio, mes, dia] = fechaPartes;

      this.year = this.getAnio(anio);
      this.mes = this.getMes(mes);
      this.dia = this.getDia(dia);
    } else {
      console.error('El formato de la fecha es inválido');
    }
  }

  getAnio(anioStr: string): number {
    const anio = parseInt(anioStr, 10);
    return isNaN(anio) ? 0 : anio;  // Devuelve 0 si la conversión falla
  }

  getDia(diaStr: string): number {
    const dia = parseInt(diaStr, 10);
    return isNaN(dia) ? 0 : dia;  // Devuelve 0 si la conversión falla
  }

  getMes(mesStr: string): string {
    const mesIndex = parseInt(mesStr, 10) - 1; // Restamos 1 porque los meses en el array comienzan en 0
    return this.meses[mesIndex] || 'Mes inválido'; // Si no existe, devolverá un mensaje de error
  }
}
