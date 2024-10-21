import { Component, OnInit } from '@angular/core';
import { CuAdminComponent } from '../../components/cu-admin/cu-admin.component';

@Component({
  standalone: true,
  imports: [CuAdminComponent],
  selector: 'app-crear-admin',
  templateUrl: './crear-admin.component.html',
  styleUrls: ['./crear-admin.component.css']
})
export class CrearAdminComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
