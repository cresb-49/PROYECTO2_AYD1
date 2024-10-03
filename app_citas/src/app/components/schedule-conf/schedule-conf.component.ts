import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Importa FormsModule para ngModel
import { CommonModule } from '@angular/common';

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
    MatCardModule,
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    NgIf,
    FormsModule // Asegúrate de importar FormsModule aquí
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

  constructor() {}

  cambiarEstado(dia: DayConfig) {
    dia.active = !dia.active; // Cambia el estado del día
    this.emitirCambio(); // Emite el evento de cambio
  }

  onHorarioChange() {
    this.emitirCambio(); // Llama a esta función cuando se editen los campos
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
}
