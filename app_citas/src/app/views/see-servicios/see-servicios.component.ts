import { Component, OnInit } from '@angular/core';
import { TableAction, TableComponent, TableHeader } from '../../components/table/table.component';
import { UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { OptionsModal } from '../../components/pop-up-modal/pop-up-modal.component';
import { PopUpModalComponent } from '../../components/pop-up-modal/pop-up-modal.component';
import { ServicioService } from '../../services/servicio/servicio.service';

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
    private servicioService: ServicioService,
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
    { name: 'Nombre', key: 'nombre' },
    { name: 'Costo', key: 'costo' },
    { name: 'Duracion (horas)', key: 'duracion' },
    { name: 'Rol Asignado', key: 'rol.nombre' },
  ];

  servicios: any[] = [];

  actions: TableAction[] = [
    { name: 'Editar', icon: 'edit', route: '/editar-servicio', key: 'id' },
    { name: 'Eliminar', icon: 'delete', action: (data: any) => this.openModal(data), return: true },
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
    this.servicioService.deleteServicio(data.id).subscribe({
      next: (response: ApiResponse) => {
        this.servicios = [];
        this.toastr.success('Servicio eliminado no exito', "Exito");
        this.getServicios();
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al eliminar el servicio');
      }
    })

  }

  getServicios() {
    this.servicioService.getServicios().subscribe(
      {
        next: (response: ApiResponse) => {
          const data = response.data;
          this.servicios = data;
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, "Error al obtener los servicios");
        }
      }
    );
  }

}
