import { Component, OnInit } from '@angular/core';
import { CuClienteComponent } from '../../components/cu-cliente/cu-cliente.component';

@Component({
  standalone: true,
  imports: [CuClienteComponent],
  selector: 'app-edit-cliente',
  templateUrl: './edit-cliente.component.html',
  styleUrls: ['./edit-cliente.component.css']
})
export class EditClienteComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
