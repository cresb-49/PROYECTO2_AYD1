import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CardAction, CardActionsComponent } from '../../components/card-actions/card-actions.component';
import { Cancha, CanchaService } from '../../services/cancha/cancha.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth/auth.service';
import { NativeUserRoles } from '../../services/auth/types';
import { OptionsModal } from '../../components/pop-up-modal/pop-up-modal.component';
import { PopUpModalComponent } from '../../components/pop-up-modal/pop-up-modal.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, CardActionsComponent, PopUpModalComponent],
  selector: 'app-see-courts',
  templateUrl: './see-courts.component.html',
  styleUrls: ['./see-courts.component.css']
})
export class SeeCourtsComponent implements OnInit {
  hideModal = true;

  optionsModal: OptionsModal = {
    question: '¿Estás seguro de que deseas eliminar esta cancha?',
    textYes: 'Sí, estoy seguro',
    textNo: 'No, cancelar',
    confirmAction: () => { }
  }

  canchas: any[] = [];
  acciones_canchas: any[] = [

  ];

  constructor(
    private canchaService: CanchaService,
    private toastr: ToastrService,
    private authService: AuthService
  ) { }

  async ngOnInit() {
    this.getCanchas();
    const isCliente = await this.isCliente();
    if (isCliente) {
      this.acciones_canchas.push({ description: 'Reservar', route: '/reservar', key: 'id', enabled: true } as CardAction);
    } else {
      this.acciones_canchas.push({ description: 'Editar', route: '/edit-cancha', key: 'id', enabled: true } as CardAction);
      this.acciones_canchas.push({ description: 'Eliminar', action: (data: any) => this.openModal(data), enabled: true } as CardAction);
    }
  }

  isCliente() {
    const roles = this.authService.getUserRoles();
    return roles.includes(NativeUserRoles.CLIENTE);
  }

  private getCanchas() {
    this.canchaService.getCanchas().subscribe({
      next: (response: ApiResponse) => {
        const data: Cancha[] = response.data ?? [];
        this.canchas = [];
        for (let d of data) {
          this.canchas.push({
            id: d.id,
            name: `Cancha ${d.id}`,
            description: d.descripcion,
            price: d.costoHora
          });
        }
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener las canchas');
      }
    });
  }

  setDescripcionAction(cancha: any) {
    return `Reservar la cancha ${cancha.name}`;
  }

  openModal(data: any) {
    this.hideModal = false;
    this.optionsModal.question = `¿Estás seguro de que deseas eliminar la cancha ${data.id}?`;
    this.optionsModal.confirmAction = () => this.deleteCourt(data);
  }

  deleteCourt(court: any) {
    this.canchaService.deleteCancha(court.id).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success('Cancha eliminada');
        this.getCanchas();
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al eliminar cancha');
      }
    });
  }


}
