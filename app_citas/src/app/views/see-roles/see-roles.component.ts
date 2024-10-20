import { Component, OnInit } from '@angular/core';
import { TableAction, TableComponent, TableHeader } from '../../components/table/table.component';
import { OptionsModal, PopUpModalComponent } from '../../components/pop-up-modal/pop-up-modal.component';
import { UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, TableComponent, PopUpModalComponent],
  selector: 'app-see-roles',
  templateUrl: './see-roles.component.html',
  styleUrls: ['./see-roles.component.css']
})
export class SeeRolesComponent implements OnInit {
  hideModal = true;
  tableHeaders: TableHeader[] = [
    { name: 'Id', key: 'id', main: true },
    { name: 'Nombre', key: 'nombre' },
  ]

  optionsModal: OptionsModal = {
    question: '¿Estás seguro de que deseas eliminar este rol?',
    textYes: 'Sí, estoy seguro',
    textNo: 'No, cancelar',
    confirmAction: () => { }
  }
  roles: any[] = [];

  actionsTable: TableAction[] = [
    { name: 'Editar', icon: 'edit', route: '/editar-rol', key: 'id' },
    { name: 'Eliminar', icon: 'delete', action: (data: any) => this.openModal(data), return: true }
  ]

  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.obtenerRoles();
  }

  obtenerRoles() {
    this.userService.getRolesGenericos().subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        this.roles = data;
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener roles');
      }
    });
  }

  openModal(data: any) {
    this.hideModal = false;
    this.optionsModal.question = `¿Estás seguro de que deseas eliminar a ${data.nombres} ${data.apellidos}?`;
    this.optionsModal.confirmAction = () => this.deleteRol(data);
  }

  deleteRol(user: any) {

  }

}
