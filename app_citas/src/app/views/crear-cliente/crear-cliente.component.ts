import { Component, OnInit } from '@angular/core';
import { CuClienteComponent } from '../../components/cu-cliente/cu-cliente.component';

@Component({
  standalone: true,
  imports: [CuClienteComponent],
  selector: 'app-crear-cliente',
  templateUrl: './crear-cliente.component.html',
  styleUrls: ['./crear-cliente.component.css']
})
export class CrearClienteComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
