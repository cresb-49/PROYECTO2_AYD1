import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CreateUserComponent } from '../../components/create-user/create-user.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, CreateUserComponent],
  selector: 'app-crear-empleado',
  templateUrl: './crear-empleado.component.html',
  styleUrls: ['./crear-empleado.component.css']
})
export class CrearEmpleadoComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
