import { Component, OnInit } from '@angular/core';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule],
  selector: 'app-agendar-cita',
  templateUrl: './agendar-cita.component.html',
  styleUrls: ['./agendar-cita.component.css']
})
export class AgendarCitaComponent implements OnInit {
  enableCita = false;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.enableCita = this.authService.isLoggedIn();
  }

}
