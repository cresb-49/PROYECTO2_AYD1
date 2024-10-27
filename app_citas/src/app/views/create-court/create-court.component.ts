import { Component, OnInit } from '@angular/core';
import { DayConfig, ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { Cancha, CanchaPayloadUpdateCreate, CanchaService, Horario } from '../../services/cancha/cancha.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { FormsModule } from '@angular/forms';
import { Dia, DiaService } from '../../services/dia/dia.service';
import { BehaviorSubject, debounceTime } from 'rxjs';
import { NegocioService } from '../../services/negocio/negocio.service';
import { HorarioService } from '../../services/horario/horario.service';

@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule, FormsModule],
  selector: 'app-create-court',
  templateUrl: './create-court.component.html',
  styleUrls: ['./create-court.component.css']
})
export class CreateCourtComponent implements OnInit {

  activeButtonSave = true;

  dataDias: Dia[] = [];

  horariosNegocio: DayConfig[] = [];

  cancha: Cancha = {
    id: 0,
    costoHora: 100,
    descripcion: '',
    horarios: []
  };

  canchaOriginal: Cancha = {
    id: 0,
    costoHora: 0,
    descripcion: '',
    horarios: []
  }

  private canchaSubject = new BehaviorSubject<Cancha>(this.cancha);

  constructor(
    private negocioService: NegocioService,
    private horarioService: HorarioService,
    private authService: AuthService,
    private canchaService: CanchaService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private diaService: DiaService
  ) { }

  async ngOnInit() {
    await this.getDias();
    await this.getHorariosNegocio();
    //Se suscribe a los cambios de los datos de la cancha
    const horario = await this.calcularHorario([]);
    this.cancha.horarios = horario;
  }

  actualizarCancha() {
    try {
      const daysActive = this.cancha.horarios.filter((day: DayConfig) => day.active);
      //Verificamos que los dias esten dentro del horario del negocio
      for (const day of daysActive) {
        this.horarioService.isDayConfigValido(this.horariosNegocio, day);
      }
      const horario: Horario[] = daysActive.map((day: DayConfig) => {
        return {
          dia: {
            id: day.id,
            nombre: day.day
          },
          apertura: day.init,
          cierre: day.end
        } as Horario
      });
      const payload: CanchaPayloadUpdateCreate = {
        cancha: {
          id: this.cancha.id,
          costoHora: this.cancha.costoHora,
          descripcion: this.cancha.descripcion
        },
        horarios: horario
      }
      //Verificamos que almenos tenga un dia en el horario
      if (horario.length <= 0) {
        throw new Error('Debe seleccionar al menos un día para la cancha');
      }
      this.canchaService.createCanche(payload).subscribe({
        next: async (response: ApiResponse) => {
          this.toastr.success('Se creo la cancha con exito!!!', 'Cancha Creada');
          this.cancha = {
            id: 0,
            costoHora: 100,
            descripcion: '',
            horarios: await this.calcularHorario([])
          };
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al crear la cancha');
        }
      });
    } catch (error: any) {
      this.toastr.error(error.message, 'Error al crear cancha');
    }

  }

  onCostoHoraChange(newData: number) {
    this.cancha.costoHora = newData;
    this.canchaSubject.next(this.cancha);
  }

  onDataChange(newData: DayConfig[]) {
    this.cancha.horarios = newData;
    this.canchaSubject.next(this.cancha);
  }

  onDetallesChange(detalle: string) {
    this.cancha.descripcion = detalle;
    this.canchaSubject.next(this.cancha);
  }

  // Comparación profunda entre dos objetos
  compararObjetos(obj1: Cancha, obj2: Cancha): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  cargarDatosCancha() {
    const id = this.route.snapshot.paramMap.get('id');
    this.canchaService.getCancha(Number(id)).subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        // Asignar los valores de la cancha
        this.cancha.id = data.id;
        this.cancha.costoHora = data.costoHora;
        this.cancha.descripcion = data.descripcion;
        this.cancha.horarios = [...this.calcularHorario(data.horarios)];
        // Guardar una copia de la cancha original
        this.canchaOriginal.id = data.id;
        this.canchaOriginal.costoHora = data.costoHora;
        this.canchaOriginal.descripcion = data.descripcion;
        this.canchaOriginal.horarios = [...this.calcularHorario(data.horarios)];
        console.log(this.cancha);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener la cancha');
      }
    })
  }

  calcularHorario(horarios: any): DayConfig[] {
    let negocioHas: DayConfig[] = horarios.map((horario: any) => {
      return {
        id: horario.dia.id,
        day: horario.dia.nombre,
        init: horario.apertura,
        end: horario.cierre,
        active: true
      } as DayConfig
    })
    //En el listado de dias buscamos por medio de id si ya esta agregado si no esta agregado
    //se agrega con active en false y init y end en 00:00
    this.dataDias.forEach((dia) => {
      let index = negocioHas.findIndex((diaHas) => {
        return diaHas.id === dia.id
      })
      if (index === -1) {
        negocioHas.push({
          id: dia.id,
          day: dia.nombre,
          init: '08:00',
          end: '18:00',
          active: false
        } as DayConfig)
      }
    })
    return negocioHas;
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

  getHorariosNegocio(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.negocioService.getInfoNegocio().subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          this.horariosNegocio = [...this.negocioService.calcularHorario(data.horarios)]
          resolve(data);
        },
        error: (error: ErrorApiResponse) => {
          reject(error)
        }
      })
    })
  }
}
