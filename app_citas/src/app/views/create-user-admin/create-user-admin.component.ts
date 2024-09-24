import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CreateUserComponent } from '../../components/create-user/create-user.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, CreateUserComponent],
  selector: 'app-create-user-admin',
  templateUrl: './create-user-admin.component.html',
  styleUrls: ['./create-user-admin.component.css']
})
export class CreateUserAdminComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
