import { Component, OnInit } from '@angular/core';
import { DayConfig, ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { NativeUserRoles } from '../../services/auth/types';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Cancha, CanchaService } from '../../services/cancha/cancha.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { ASOCIACION_DIAS_NOMBRE, Dia, DiaService } from '../../services/dia/dia.service';
import { FormsModule } from '@angular/forms';
import { HorarioService } from '../../services/horario/horario.service';
import { NegocioService } from '../../services/negocio/negocio.service';
import { ReservaCancha, ReservaService } from '../../services/reserva/reserva.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule, FormsModule],
  selector: 'app-reservar-cancha',
  templateUrl: './reservar-cancha.component.html',
  styleUrls: ['./reservar-cancha.component.css']
})
export class ReservarCanchaComponent implements OnInit {

  pdfUrl: SafeResourceUrl | null = null;

  enableReserve = false;

  dataDias: Dia[] = [];

  negocio: any = null;
  porcentaje_anticipo = 0;

  cancha: Cancha = {
    id: 0,
    costoHora: 0,
    descripcion: '',
    horarios: []
  };

  cardInfo: any = {
    name: '',
    number: '',
    fecha: '',
    cvv: ''
  }

  fecha_cita = null
  inicio_cita = ''
  minutes_inicio_cita = 0
  minutes_fin_cita = 0
  minutos_totales_reserva = 0
  fin_cita = ''

  constructor(
    private sanitizer: DomSanitizer,
    private reservaService: ReservaService,
    private negocioService: NegocioService,
    private authService: AuthService,
    private canchaService: CanchaService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private horarioService: HorarioService,
    private diaService: DiaService
  ) { }

  async ngOnInit() {
    const roles = this.authService.getUserRoles();
    const isClient = roles.includes(NativeUserRoles.CLIENTE);
    this.enableReserve = this.authService.isLoggedIn() && roles.includes(NativeUserRoles.CLIENTE);
    await this.getDias();
    await this.cargarDatosCancha();
    await this.getInfoNegocio();

  }

  inicioChange(value: string) {
    // Convierte la hora de inicio en minutos
    const [hours, minutes] = value.split(':').map(Number);
    this.minutes_inicio_cita = hours * 60 + minutes;
    console.log('Inicio:', this.minutes_inicio_cita);
    this.minutos_totales_reserva = this.minutes_fin_cita - this.minutes_inicio_cita;
    if (this.minutos_totales_reserva < 0) {
      this.toastr.error('La hora de inicio no puede ser mayor a la hora de fin');
      this.minutos_totales_reserva = 0;
    }
  }

  finChange(value: string) {
    // Convierte la hora de inicio en minutos
    const [hours, minutes] = value.split(':').map(Number);
    this.minutes_fin_cita = hours * 60 + minutes;
    console.log('Fin:', this.minutes_fin_cita);
    this.minutos_totales_reserva = this.minutes_fin_cita - this.minutes_inicio_cita;
    if (this.minutos_totales_reserva < 0) {
      this.toastr.error('La hora de fin no puede ser menor a la hora de inicio');
      this.minutos_totales_reserva = 0;
    }
  }

  generarReserva() {
    console.log('Generar reserva');
    const [year, month, day] = (this.fecha_cita ?? '').split('-').map(Number); // Dividimos la fecha en partes
    const fechaSeleccionada = new Date(year, month - 1, day); // Crear la fecha seleccionada correctamente
    const fechaActual = new Date(); // Fecha actual
    // Normalizamos ambas fechas a medianoche
    fechaSeleccionada.setHours(0, 0, 0, 0);
    fechaActual.setHours(0, 0, 0, 0);
    // Extraemos el día de la fecha
    const diaSemana = fechaSeleccionada.getDay();
    const nombreDiaSeleccionado = ASOCIACION_DIAS_NOMBRE.get(diaSemana);
    const diaSeleccionado = this.dataDias.find((dia: Dia) => dia.nombre === nombreDiaSeleccionado)
    try {
      if (!diaSeleccionado) {
        throw new Error('Debe de seleccionar una fecha para la cita')
      }
      if (fechaSeleccionada < fechaActual) {
        throw new Error('La fecha seleccionada es anterior a la fecha actual.');
      }
      if (this.inicio_cita === null || this.inicio_cita === undefined || this.inicio_cita === '') {
        throw new Error('Debe de configurar una hora para la cita')
      }
      //Verificamos que el dia configurado exista en el horario
      const configuracionDiaSeleccionado: DayConfig = {
        id: diaSeleccionado.id,
        active: true,
        day: diaSeleccionado.nombre,
        init: this.inicio_cita,
        end: this.fin_cita
      }
      this.horarioService.isDayConfigValido(this.cancha.horarios, configuracionDiaSeleccionado)

      // this.validateCardInfo(this.cardInfo);
      const payload: ReservaCancha = {
        canchaId: this.cancha.id,
        horaInicio: this.inicio_cita,
        horaFin: this.fin_cita,
        fechaReservacion: this.fecha_cita ?? '',
      }

      this.reservaService.reservarChancha(payload, 'blob').subscribe({
        next: (response: Blob | any) => {
          this.toastr.success(response.message, 'Rerserva Completada!!!')
          const blob = new Blob([response], { type: 'application/pdf' });
          const url = window.URL.createObjectURL(blob);
          this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
        },
        error: (error: ErrorApiResponse) => {
          console.log(error);
          this.toastr.error(error.error, 'Error al realizar la reserva!!!')
        }
      });
      console.log(payload);
    } catch (error: any) {
      this.toastr.error(error.message, 'Error al agendar la cita');
    }
  }

  getInfoNegocio(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.negocioService.getInfoNegocio().subscribe(
        {
          next: (response: ApiResponse) => {
            this.negocio = response.data;
            this.porcentaje_anticipo = this.negocio.porcentajeAnticipo ?? 0;
          },
          error: (error: ErrorApiResponse) => {
            this.toastr.error(error.error, 'Error al obtener la info de usuario');
            reject(error)
          }
        }
      );
    });
  }

  validateCardInfo(cardInfo: any): void {
    const errors: string[] = [];

    // Validar nombre
    if (!cardInfo.name || cardInfo.name.trim() === '') {
      throw new Error('El nombre es requerido.');
    }

    // Validar número de tarjeta (Ejemplo: Debe tener 16 dígitos)
    const cardNumberPattern = /^[0-9]{16}$/;
    if (!cardInfo.number || !cardNumberPattern.test(cardInfo.number)) {
      throw new Error('El número de tarjeta es inválido. Debe contener 16 dígitos.');
    }

    // Validar fecha de expiración (Formato MM/YY)
    const expirationDatePattern = /^(0[1-9]|1[0-2])\/\d{2}$/;
    if (!cardInfo.fecha || !expirationDatePattern.test(cardInfo.fecha)) {
      throw new Error('La fecha de expiración es inválida. Debe tener el formato MM/YY.');
    }

    // Validar CVV (Ejemplo: Debe tener 3 dígitos)
    const cvvPattern = /^[0-9]{3}$/;
    if (!cardInfo.cvv || !cvvPattern.test(cardInfo.cvv)) {
      throw new Error('El CVV es inválido. Debe contener 3 dígitos.');
    }
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
    return negocioHas;
  }

  getDias() {
    this.diaService.getDias().subscribe({
      next: (response: ApiResponse) => {
        const dias = response.data ?? [];
        dias.forEach((dia: any) => {
          this.dataDias.push({
            id: dia.id,
            nombre: dia.nombre,
          } as Dia);
        });
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.message, 'Error al obtener los días');
      }
    });
  }

}
