import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Importa FormsModule para ngModel

@Component({
  standalone: true,
  selector: 'app-schedule-conf',
  templateUrl: './schedule-conf.component.html',
  styleUrls: ['./schedule-conf.component.css'],
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    NgIf,
    FormsModule // Asegúrate de importar FormsModule aquí
  ],
})
export class ScheduleConfComponent {
  @Input() day: string = 'Miercoles';

  // Inputs para las horas con valores por defecto
  @Input() start: string = '08:00';
  @Input() end: string = '18:00';

  constructor() {}
}
