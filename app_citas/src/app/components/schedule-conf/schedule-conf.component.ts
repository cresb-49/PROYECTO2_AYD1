import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Importa FormsModule para ngModel
import { CommonModule } from '@angular/common';

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
  @Input() data = [
    { day: 'Lunes', init: '08:00', end: '18:00', active: true },
    { day: 'Martes', init: '08:00', end: '18:00', active: true },
    { day: 'Miércoles', init: '08:00', end: '18:00', active: true },
    { day: 'Jueves', init: '08:00', end: '18:00', active: true },
    { day: 'Viernes', init: '08:00', end: '18:00', active: true },
    { day: 'Sábado', init: '08:00', end: '18:00', active: false },
    { day: 'Domingo', init: '08:00', end: '18:00', active: false },
  ];

  constructor() { }

  cambiarEstado(dia: any) {
    dia.active = !dia.active;
  }
}
