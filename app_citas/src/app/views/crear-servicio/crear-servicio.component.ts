import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ScheduleConfComponent],
  selector: 'app-crear-servicio',
  templateUrl: './crear-servicio.component.html',
  styleUrls: ['./crear-servicio.component.css']
})
export class CrearServicioComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
