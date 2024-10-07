import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CardActionsComponent } from '../../components/card-actions/card-actions.component';
import { Cancha, CanchaService } from '../../services/cancha/cancha.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, CardActionsComponent],
  selector: 'app-see-courts',
  templateUrl: './see-courts.component.html',
  styleUrls: ['./see-courts.component.css']
})
export class SeeCourtsComponent implements OnInit {
  canchas = [
    { id: 1, name: 'Cancha 1', description: 'Cancha de futbol', price: 100 },
    { id: 2, name: 'Cancha 2', description: 'Cancha de futbol', price: 200 },
    { id: 3, name: 'Cancha 3', description: 'Cancha de futbol', price: 300 },
    { id: 4, name: 'Cancha 4', description: 'Cancha de futbol', price: 400 },
    { id: 5, name: 'Cancha 5', description: 'Cancha de futbol', price: 500 },
  ];
  acciones_canchas = [
    { description: 'Reservar', route: '/reservar-cancha', enabled: true },
    { description: 'Modificar', route: '/edit-cancha', enabled: true },
  ];

  constructor(
    private canchaService: CanchaService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.getCanchas();
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
