import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CreateUserComponent } from '../../components/create-user/create-user.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, CreateUserComponent],
  selector: 'app-create-user-admin-negocio',
  templateUrl: './create-user-admin-negocio.component.html',
  styleUrls: ['./create-user-admin-negocio.component.css']
})
export class CreateUserAdminNegocioComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
