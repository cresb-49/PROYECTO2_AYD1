import { Component, OnInit } from '@angular/core';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { ManageServicio } from '../../components/cu-servicio/cu-servicio.component';
import { ServicioService } from '../../services/servicio/servicio.service';
import { ActivatedRoute } from '@angular/router';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule, FormsModule],
  selector: 'app-agendar-cita',
  templateUrl: './agendar-cita.component.html',
  styleUrls: ['./agendar-cita.component.css']
})
export class AgendarCitaComponent implements OnInit {
  enableCita = false;
  servicioData: ManageServicio = {
    imagen: '',
    nombre: '',
    detalles: '',
    precio: 1,
    duracion: 0.25,
    id_rol: 0
  };

  constructor(private authService: AuthService,
    private servicioService: ServicioService,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  async ngOnInit() {
    this.enableCita = this.authService.isLoggedIn();
    await this.cargarDatosServicio();
  }

  cargarDatosServicio(): Promise<void> {
    return new Promise((resolve, reject) => {
      //Obtenemos el paramento id de la url
      const id = this.route.snapshot.paramMap.get('id');
      this.servicioService.getServicio(id).subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          this.servicioData = {
            id: data.id,
            detalles: data.detalles,
            duracion: data.duracion,
            id_rol: data.rol.id,
            imagen: data.imagen,
            nombre: data.nombre,
            precio: data.costo
          }
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener el servicio')
          reject(error);
        }
      })
    });
  }

  agendar(){
    
  }
}
