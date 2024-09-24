import { Component, OnInit } from '@angular/core';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule],
  selector: 'app-reservar-cancha',
  templateUrl: './reservar-cancha.component.html',
  styleUrls: ['./reservar-cancha.component.css']
})
export class ReservarCanchaComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
