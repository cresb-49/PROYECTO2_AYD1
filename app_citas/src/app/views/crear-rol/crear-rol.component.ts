import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CuRolComponent } from '../../components/cu-rol/cu-rol.component';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, CuRolComponent],
  selector: 'app-crear-rol',
  templateUrl: './crear-rol.component.html',
  styleUrls: ['./crear-rol.component.css']
})
export class CrearRolComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
