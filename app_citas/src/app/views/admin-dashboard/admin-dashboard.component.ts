import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CardButtonComponent } from '../../components/card-button/card-button.component';
import { GraficosComponent } from '../graficos/graficos.component';

@Component({
  standalone: true,
  imports: [RouterModule, CommonModule, CardButtonComponent, GraficosComponent],
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
