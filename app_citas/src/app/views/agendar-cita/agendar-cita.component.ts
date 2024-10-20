import { Component, OnInit } from '@angular/core';
import { DayConfig, ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
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

  empleados: any[] = [];
  horario: DayConfig[] = [];

  constructor(private authService: AuthService,
    private servicioService: ServicioService,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  async ngOnInit() {
    this.enableCita = this.authService.isLoggedIn();
    await this.cargarDatosServicio();
    await this.empleadosServicio();
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
          resolve(data)
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener el servicio')
          reject(error);
        }
      })
    });
  }

  empleadosServicio(): Promise<void> {
    return new Promise((resolve, reject) => {
      const id = this.route.snapshot.paramMap.get('id');
      this.servicioService.getEmpleadosServicio(Number(id)).subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          this.empleados = data;
          this.procesadoHorarios(this.procesarHoarioEmpleado(data));
          resolve(data);
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener los empleados');
          reject(error)
        }
      });
    });
  }

  agendar() {

  }

  procesarHoarioEmpleado(data: any[]): DayConfig[][] {
    let horariosEmpleados = data.map(single_data => {
      const result = ((single_data.horarios ?? []) as Array<any>).map(horario => {
        const dayConf: DayConfig = {
          id: horario.dia.id,
          day: horario.dia.nombre,
          init: horario.entrada,
          end: horario.salida,
          active: true,
        }
        return dayConf
      })
      return result
    })
    return horariosEmpleados;
  }

  procesadoHorarios(horariosEmpleados: DayConfig[][]) {
    let diasServicio: Map<number, DayConfig> = new Map;
    console.log(horariosEmpleados);
    (horariosEmpleados ?? []).forEach(horarioEmpleado => {
      (horarioEmpleado ?? []).forEach(dayconf => {
        //Buscamos en el diasServicio si esta el id
        let result = diasServicio.get(dayconf.id);
        if (result) {
          //Comparamos la hora init y la hora end
          //Si la hora init es menor a la que ya esta asignanos se asigna ese valor
          //Si la hora end es mayor a la que ya esta asignada se asigna ese valor
          //Abos valores estan en string pero en formado HH:mm de 24 horas
          //Comparamos la hora init y la hora end
          //Convertimos a formato Date los valores de init y end para comparar correctamente
          const initResult = new Date(`1970-01-01T${result.init}:00`);
          const endResult = new Date(`1970-01-01T${result.end}:00`);
          const initNew = new Date(`1970-01-01T${dayconf.init}:00`);
          const endNew = new Date(`1970-01-01T${dayconf.end}:00`);
          // Si la hora init del nuevo es menor, la asignamos
          if (initNew < initResult) {
            result.init = dayconf.init;
          }
          // Si la hora end del nuevo es mayor, la asignamos
          if (endNew > endResult) {
            result.end = dayconf.end;
          }
        } else {
          //Si no se encuentra agregamos una copia de los datos
          diasServicio.set(dayconf.id, { ...dayconf })
        }
      })
    })
    this.horario = Array.from(diasServicio.values());
  }
}
