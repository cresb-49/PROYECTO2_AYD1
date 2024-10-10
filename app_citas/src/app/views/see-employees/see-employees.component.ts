import { Component, OnInit } from '@angular/core';
import { TableAction, TableComponent, TableHeader } from '../../components/table/table.component';
import { UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';

export interface EmpleadoTable {
  id: number;
  nombres: string;
  apellidos: string;
  email: string;
  roles: string;
}
@Component({
  standalone: true,
  imports: [TableComponent, CommonModule],
  selector: 'app-see-employees',
  templateUrl: './see-employees.component.html',
  styleUrls: ['./see-employees.component.css']
})
export class SeeEmployeesComponent implements OnInit {

  tableHeaders: TableHeader[] = [
    { name: 'Id', key: 'id', main: true },
    { name: 'Nombres', key: 'nombres' },
    { name: 'Apellidos', key: 'apellidos' },
    { name: 'Email', key: 'email' },
    { name: 'Rol', key: 'roles' },
    { name: 'Acciones', key: 'actions' }
  ];

  empleados: any[] = [];

  actions: TableAction[] = [
    { name: 'Edit', icon: 'edit', route: '/edit-empleado', key: 'name' },
    { name: 'Delete', icon: 'delete', action: () => { }, return: true },
  ]


  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.getEmpleados();
  }

  getEmpleados() {
    this.userService.getEmpleados().subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        this.empleados = this.processEmpleados(data);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener empleados');
      }
    })
  }

  processEmpleados(empleados: any[]): EmpleadoTable[] {
    let result: EmpleadoTable[] = [];
    for (const empleado of empleados) {
      const roles = empleado.usuario.roles.map((rol: any) => rol.rol.nombre).join(', ');
      result.push({
        id: empleado.id,
        nombres: empleado.usuario.nombres,
        apellidos: empleado.usuario.apellidos,
        email: empleado.usuario.email,
        roles: roles
      });
    }
    return result;
  }

}
