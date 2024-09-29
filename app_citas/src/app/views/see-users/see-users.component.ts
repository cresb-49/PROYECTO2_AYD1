import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { TableComponent, TableHeader } from '../../components/table/table.component';


@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, TableComponent],
  selector: 'app-see-users',
  templateUrl: './see-users.component.html',
  styleUrls: ['./see-users.component.css']
})
export class SeeUsersComponent implements OnInit {

  usuarios: any[] = [];
  tableHeaders: TableHeader[] = [
    { name: 'Nombre', key: 'name', main: true },
    { name: 'Apellido', key: 'lastname' },
    { name: 'Correo', key: 'email' },
    { name: 'Rol', key: 'role' },
  ]
  constructor(private userService: UserService) { }

  ngOnInit() {
    this.obtenerUsuarios();
  }

  obtenerUsuarios() {
    this.usuarios = this.userService.getUsers();
  }


}
