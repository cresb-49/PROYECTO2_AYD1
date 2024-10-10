import { Component, OnInit } from '@angular/core';
import { TableAction, TableComponent, TableHeader } from '../../components/table/table.component';
import { UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { OptionsModal } from '../../components/pop-up-modal/pop-up-modal.component';
import { PopUpModalComponent } from '../../components/pop-up-modal/pop-up-modal.component';

@Component({
  standalone: true,
  imports: [TableComponent, CommonModule, PopUpModalComponent],
  selector: 'app-see-servicios',
  templateUrl: './see-servicios.component.html',
  styleUrls: ['./see-servicios.component.css']
})
export class SeeServiciosComponent implements OnInit {

  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) { }

  hideModal = true;

  optionsModal: OptionsModal = {
    question: '¿Estás seguro de que deseas eliminar este empleado?',
    textYes: 'Sí, estoy seguro',
    textNo: 'No, cancelar',
    confirmAction: () => { }
  }

  tableHeaders: TableHeader[] = [
    { name: 'Id', key: 'id', main: true },
    { name: 'Nombre', key: 'nombres' },
    { name: 'Costo', key: 'apellidos' },
    { name: 'Duracion', key: 'email' },
    { name: 'Rol Asignado', key: 'roles' },
  ];

  servicios: any[] = [];

  actions: TableAction[] = [
    { name: 'Edit', icon: 'edit', route: '/edit-empleado', key: 'id' },
    { name: 'Delete', icon: 'delete', action: (data: any) => this.openModal(data), return: true },
  ]

  ngOnInit() {
    this.getServicios();
  }

  openModal(data: any) {
    this.hideModal = false;
    this.optionsModal.question = `¿Estás seguro de que deseas eliminar el servicio ${data.nombre}?`;
    this.optionsModal.confirmAction = () => this.deleteServicio(data);
  }

  deleteServicio(data: any) {

  }

  getServicios() {
  }

}
