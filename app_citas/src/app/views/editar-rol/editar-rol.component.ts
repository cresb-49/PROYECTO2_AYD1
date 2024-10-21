import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CuRolComponent } from '../../components/cu-rol/cu-rol.component';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, CuRolComponent],
  selector: 'app-editar-rol',
  templateUrl: './editar-rol.component.html',
  styleUrls: ['./editar-rol.component.css']
})
export class EditarRolComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
