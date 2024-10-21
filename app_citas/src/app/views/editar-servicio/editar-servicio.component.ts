import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CuServicioComponent } from '../../components/cu-servicio/cu-servicio.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ScheduleConfComponent, CuServicioComponent],
  selector: 'app-editar-servicio',
  templateUrl: './editar-servicio.component.html',
  styleUrls: ['./editar-servicio.component.css']
})
export class EditarServicioComponent implements OnInit {
  modificar = true;
  constructor() { }

  ngOnInit() {
  }

}
