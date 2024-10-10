import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { TableAction, TableComponent, TableHeader } from '../../components/table/table.component';
import { OptionsModal, PopUpModalComponent } from '../../components/pop-up-modal/pop-up-modal.component';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';

export interface UserInfo {
  id: number;
  nombres: string;
  apellidos: string;
  email: string;
}

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, TableComponent, PopUpModalComponent],
  selector: 'app-see-users',
  templateUrl: './see-users.component.html',
  styleUrls: ['./see-users.component.css']
})
export class SeeUsersComponent implements OnInit {
  hideModal = true;
  tableHeaders: TableHeader[] = [
    { name: 'Nombre', key: 'nombres', main: true },
    { name: 'Apellido', key: 'apellidos' },
    { name: 'Correo', key: 'email' }
  ]

  optionsModal: OptionsModal = {
    question: '¿Estás seguro de que deseas eliminar este usuario?',
    textYes: 'Sí, estoy seguro',
    textNo: 'No, cancelar',
    confirmAction: () => { }
  }

  usuarios: UserInfo[] = [];

  actionsTable: TableAction[] = [
    { name: 'Editar', icon: 'edit', route: '/edit-user', key: 'id' },
    { name: 'Eliminar', icon: 'delete', action: (data: any) => this.openModal(data), return: true }
  ]
  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.obtenerUsuarios();
  }

  obtenerUsuarios() {
    this.userService.getUsers().subscribe({
      next: (response: ApiResponse) => {
        console.log('Response:', response);
        const data = response.data;
        this.usuarios = [];
        for(let d of data) {
          this.usuarios.push({
            id: d.id,
            nombres: d.nombres,
            apellidos: d.apellidos,
            email: d.email
          });
        }
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener los usuarios');
      }
    });
  }

  openModal(data: any) {
    this.hideModal = false;
    this.optionsModal.question = `¿Estás seguro de que deseas eliminar a ${data.name} ${data.lastname}?`;
    this.optionsModal.confirmAction = () => this.deteleUser(data);
  }

  deteleUser(user: any) {
    this.userService.elimiarUsuario(user.id).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success('Usuario eliminado');
        this.obtenerUsuarios();
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al eliminar usuario');
      }
    });
  }

}
