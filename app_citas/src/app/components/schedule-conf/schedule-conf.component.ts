import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Importa FormsModule para ngModel
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';

export interface DayConfig {
  id: number;
  day: string;
  init: string;
  end: string;
  active: boolean;
}

@Component({
  standalone: true,
  selector: 'app-schedule-conf',
  templateUrl: './schedule-conf.component.html',
  styleUrls: ['./schedule-conf.component.css'],
  imports: [
    CommonModule,
    FormsModule
  ],
})
export class ScheduleConfComponent {
  @Input() isShowData = false;
  @Input() showNonActive = true;
  @Input() data: DayConfig[] = [
    { id: 1, day: 'Lunes', init: '08:00', end: '18:00', active: true },
    { id: 2, day: 'Martes', init: '08:00', end: '18:00', active: true },
    { id: 3, day: 'Miércoles', init: '08:00', end: '18:00', active: true },
    { id: 4, day: 'Jueves', init: '08:00', end: '18:00', active: true },
    { id: 5, day: 'Viernes', init: '08:00', end: '18:00', active: true },
    { id: 6, day: 'Sábado', init: '08:00', end: '18:00', active: false },
    { id: 7, day: 'Domingo', init: '08:00', end: '18:00', active: false },
  ];

  // Emisor de eventos de cambios en la data
  @Output() dataChange = new EventEmitter<DayConfig[]>();

  constructor(
    private toastr: ToastrService
  ) { }

  cambiarEstado(dia: DayConfig) {
    dia.active = !dia.active; // Cambia el estado del día
    this.emitirCambio(); // Emite el evento de cambio
  }

  onHorarioChange(dia: DayConfig) {
    if (this.horaInicioMayorQueHoraFin(dia.init, dia.end)) {
      dia.init = this.restarUnMinuto(dia.end); // Restamos un minuto a la hora de fin y la ponemos como inicio
      this.toastr.error(`La hora de inicio no puede ser mayor que la hora de fin para el día ${dia.day}. Se ajustará la hora de inicio.`);
    }
    this.emitirCambio(); // Emite el evento después de cualquier cambio
  }

  // Función para restar un minuto a una hora dada
  restarUnMinuto(hora: string): string {
    const [horas, minutos] = hora.split(':').map(Number);
    const fecha = new Date(0, 0, 0, horas, minutos);
    fecha.setMinutes(fecha.getMinutes() - 1); // Restamos un minuto

    // Formateamos las horas y minutos para que siempre tengan dos dígitos
    const horasAjustadas = fecha.getHours().toString().padStart(2, '0');
    const minutosAjustados = fecha.getMinutes().toString().padStart(2, '0');

    return `${horasAjustadas}:${minutosAjustados}`;
  }

  emitirCambio() {
    this.dataChange.emit(this.data); // Emite el evento con la data actualizada
  }

  filtroDataMostrar() {
    if (this.showNonActive) {
      return this.data;
    }
    return this.data.filter((dia: DayConfig) => dia.active);
  }

  // Función para verificar si la hora de inicio es mayor que la hora de fin
  horaInicioMayorQueHoraFin(init: string, end: string): boolean {
    const [initHoras, initMinutos] = init.split(':').map(Number);
    const [endHoras, endMinutos] = end.split(':').map(Number);
    const horaInicio = new Date(0, 0, 0, initHoras, initMinutos);
    const horaFin = new Date(0, 0, 0, endHoras, endMinutos);

    return horaInicio.getTime() > horaFin.getTime();
  }
}
