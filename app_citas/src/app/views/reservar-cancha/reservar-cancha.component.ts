import { Component, OnInit } from '@angular/core';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { NativeUserRoles } from '../../services/auth/types';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule],
  selector: 'app-reservar-cancha',
  templateUrl: './reservar-cancha.component.html',
  styleUrls: ['./reservar-cancha.component.css']
})
export class ReservarCanchaComponent implements OnInit {
  enableReserve = false;

  constructor(
    private authService: AuthService
  ) { }

  ngOnInit() {
    const roles = this.authService.getUserRoles();
    const isClient = roles.includes(NativeUserRoles.CLIENTE);
    this.enableReserve = this.authService.isLoggedIn() && roles.includes(NativeUserRoles.CLIENTE);
    console.log('enableReserve:', this.enableReserve);
  }

}
