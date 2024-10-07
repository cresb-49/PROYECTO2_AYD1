import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CardActionsComponent } from '../../components/card-actions/card-actions.component';
import { Cancha, CanchaService } from '../../services/cancha/cancha.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth/auth.service';
import { NativeUserRoles } from '../../services/auth/types';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, CardActionsComponent],
  selector: 'app-see-courts',
  templateUrl: './see-courts.component.html',
  styleUrls: ['./see-courts.component.css']
})
export class SeeCourtsComponent implements OnInit {
  canchas: any[] = [];
  acciones_canchas:any[] = [

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
      this.acciones_canchas.push({ description: 'Reservar', route: '/reservar-cancha', enabled: true });
    }else{
      this.acciones_canchas.push({ description: 'Editar', route: '/edit-cancha', enabled: true });
      this.acciones_canchas.push({ description: 'Eliminar', route: '/eliminar-cancha', enabled: true });
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

  setValueAction(cancha: any): any[] {
    //Clonamos el arreglo de acciones
    let acciones: any[] = [];

    //Iteramos el arreglo de acciones para agregar el id de la cancha
    this.acciones_canchas.forEach(accion => {
      //Clonamos la accion
      let accion_clon = { ...accion };

      //Asignamos la ruta de la accion
      accion_clon.route += `/${cancha.id}`;

      //Asignamos la accion al arreglo de acciones
      acciones.push(accion_clon);
    });
    //Asignamos el nuevo arreglo de acciones
    return acciones;
  }

  setDescripcionAction(cancha: any) {
    return `Reservar la cancha ${cancha.name}`;
  }

}
