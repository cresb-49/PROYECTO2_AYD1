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
import { HorarioService } from '../../services/horario/horario.service';
import { ASOCIACION_DIAS_NOMBRE, Dia, DiaService } from '../../services/dia/dia.service';

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

  dataDias: Dia[] = []
  empleados: any[] = [];
  horario: DayConfig[] = [];

  selected_id_empleado = 0;
  fecha_cita = null
  inicio_cita = ''
  fin_cita = ''

  constructor(
    private diaService: DiaService,
    private authService: AuthService,
    private servicioService: ServicioService,
    private horarioService: HorarioService,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  async ngOnInit() {
    this.enableCita = this.authService.isLoggedIn();
    await this.cargarDatosServicio();
    await this.empleadosServicio();
    await this.getDias();
  }

  inicioChange(value: string) {
    // Convierte la hora de inicio en minutos
    const [hours, minutes] = value.split(':').map(Number);
    let totalMinutes = hours * 60 + minutes;

    // Convierte la duración de horas a minutos (ejemplo: 0.25 horas = 15 minutos)
    const durationInMinutes = this.servicioData.duracion * 60;

    // Suma la duración a los minutos totales
    totalMinutes += durationInMinutes;

    // Calcula las nuevas horas y minutos
    const finalHours = Math.floor(totalMinutes / 60) % 24; // Para manejar valores mayores a 24 horas
    const finalMinutes = Math.floor(totalMinutes % 60);

    // Formatear la hora final (añadiendo ceros si es necesario)
    const formattedHours = String(finalHours).padStart(2, '0');
    const formattedMinutes = String(finalMinutes).padStart(2, '0');

    // Asignamos el valor calculado a la variable de binding `fin_cita`
    this.fin_cita = `${formattedHours}:${formattedMinutes}`;
  }

  getDias(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.diaService.getDias().subscribe({
        next: (response: ApiResponse) => {
          const dias = response.data ?? [];
          dias.forEach((dia: any) => {
            this.dataDias.push({
              id: dia.id,
              nombre: dia.nombre,
            } as Dia);
          });
          resolve(); // Resolvemos la promesa cuando se obtienen los días
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.message, 'Error al obtener los días');
          reject(error);
        }
      });
    });
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
    // Suponiendo que this.fecha_cita contiene una fecha en formato 'YYYY-MM-DD'
    const fecha = new Date(this.fecha_cita ?? '');
    // Extraemos el día de la fecha
    const diaSemana = fecha.getDay();
    const nombreDiaSeleccionado = ASOCIACION_DIAS_NOMBRE.get(diaSemana);
    const diaSeleccionado = this.dataDias.find((dia: Dia) => dia.nombre === nombreDiaSeleccionado)
    try {
      if (!diaSeleccionado) {
        throw new Error('Debe de seleccionar una fecha para la cita')
      }
      if (this.inicio_cita === null || this.inicio_cita === undefined || this.inicio_cita === '') {
        throw new Error('Debe de configurar una hora para la cita')
      }
      const configuracionDiaSeleccionado: DayConfig = {
        id: diaSeleccionado.id,
        active: true,
        day: diaSeleccionado.nombre,
        init: this.inicio_cita,
        end: this.fin_cita
      }
      //Verificamos que el dia configurado exista en el horario
      this.horarioService.isDayConfigValido(this.horario, configuracionDiaSeleccionado)
      const payload = {
        fecha: this.fecha_cita,
        id_empelado: this.selected_id_empleado === 0 ? null : this.selected_id_empleado,
        inicio: this.inicio_cita,
        fin: this.fin_cita
      }
      console.log(payload);
    } catch (error: any) {
      this.toastr.error(error.message, 'Error al agendar la cita');
    }
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
