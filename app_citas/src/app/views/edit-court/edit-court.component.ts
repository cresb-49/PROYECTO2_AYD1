import { Component, OnInit } from '@angular/core';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { Cancha, CanchaService } from '../../services/cancha/cancha.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { FormsModule } from '@angular/forms';
@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule, FormsModule],
  selector: 'app-edit-court',
  templateUrl: './edit-court.component.html',
  styleUrls: ['./edit-court.component.css']
})
export class EditCourtComponent implements OnInit {
  cancha: Cancha = {
    id: 0,
    costoHora: 0,
    descripcion: '',
    horarios: []
  };

  canchaOriginal: Cancha = {
    id: 0,
    costoHora: 0,
    descripcion: '',
    horarios: []
  }

  constructor(
    private authService: AuthService,
    private canchaService: CanchaService,
    private toastr: ToastrService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    // Recuperar el valor del parámetro de la ruta
    const id = this.route.snapshot.paramMap.get('id');
    console.log(id); // Aquí puedes ver el valor del id
    this.canchaService.getCancha(Number(id)).subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        // Asignar los valores de la cancha
        this.cancha.id = data.id;
        this.cancha.costoHora = data.costoHora;
        this.cancha.descripcion = data.descripcion;
        this.cancha.horarios = data.horarios;
        // Guardar una copia de la cancha original
        this.canchaOriginal.id = data.id;
        this.canchaOriginal.costoHora = data.costoHora;
        this.canchaOriginal.descripcion = data.descripcion;
        this.canchaOriginal.horarios = data.horarios;
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener la cancha');
      }
    })
  }

}
