import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { ReservaService } from '../../services/reserva/reserva.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';
import { ReservaResponse } from '../../components/calendar-components/calendar/calendar.component';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-procesar-cita',
  templateUrl: './procesar-cita.component.html',
  styleUrls: ['./procesar-cita.component.css']
})
export class ProcesarCitaComponent implements OnInit {

  pdfUrl: SafeResourceUrl | null = null;

  pago_tarjeta: boolean = true;
  curentReserva: ReservaResponse = {
    id: 0,
    reservador: {
      id: 0,
      nombres: '',
      apellidos: ''
    },
    horaInicio: '',
    horaFin: '',
    fechaReservacion: '',
    idFactura: 0,
    realizada: false,
    canceledAt: '',
    adelanto: 0,
    totalACobrar: 0,
    reservaCancha: {
      id: 0,
      cancha: {
        id: 0,
        descripcion: ''
      }
    },
    reservaServicio: {
      id: 0,
      servicio: {
        id: 0,
        nombre: '',
        detalles: ''
      }
    }
  };
  cardInfo: any = {
    name: '',
    number: '',
    fecha: '',
    cvv: ''
  }
  constructor(
    private sanitizer: DomSanitizer,
    private authService: AuthService,
    private reservaService: ReservaService,
    private toastr: ToastrService,
    private route: ActivatedRoute
  ) { }

  async ngOnInit() {
    this.curentReserva = await this.cargarDatosRerserva();
    console.log('Reserva:', this.curentReserva);

  }

  cargarDatosRerserva(): Promise<ReservaResponse> {
    //Obtenemos el id de la reserva en la url de la pagina
    return new Promise((resolve, reject) => {
      const id_reserva = this.route.snapshot.paramMap.get('id');

      let reserva: ReservaResponse;
      this.reservaService.reservaById(Number(id_reserva)).subscribe(
        {
          next: (res: ApiResponse) => {
            const data = res.data;
            reserva = {
              id: data.id,
              reservador: {
                id: data.reservador.id,
                nombres: data.reservador.nombres,
                apellidos: data.reservador.apellidos,
                nit: data.reservador.nit,
              },
              horaInicio: data.horaInicio,
              horaFin: data.horaFin,
              fechaReservacion: data.fechaReservacion,
              idFactura: data.idFactura,
              realizada: data.realizada,
              canceledAt: data.canceledAt,
              adelanto: data.adelanto,
              totalACobrar: data.totalACobrar,
              reservaCancha: data.reservaCancha ? {
                id: data.reservaCancha.id,
                cancha: {
                  id: data.reservaCancha.cancha.id,
                  descripcion: data.reservaCancha.cancha.descripcion
                }
              } : null,
              reservaServicio: data.reservaServicio ? {
                id: data.reservaServicio.id,
                servicio: {
                  id: data.reservaServicio.servicio.id,
                  nombre: data.reservaServicio.servicio.nombre,
                  detalles: data.reservaServicio.servicio.detalles,
                }
              } : null
            }
            resolve(reserva);
          },
          error: (err: ErrorApiResponse) => {
            this.toastr.error(err.error, 'Error');
            reject(err);
          }
        }
      );
    });
  }

  onPagoTarjetaChange() {
    this.pago_tarjeta = !this.pago_tarjeta;
  }

  procesarReserva() {
    try {
      //Si el pago es con tarjeta debe validar los datos de la tarjeta
      if (this.pago_tarjeta) {
        this.validateCardInfo(this.cardInfo);
      }
      //Damos por terminada la reserva
      this.reservaService.procesarReserva(this.curentReserva.id).subscribe(
        {
          next: (response: Blob | any) => {
            this.toastr.success(response.message, 'Rerserva Completada!!!')
            const blob = new Blob([response], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
            this.limpiarCampos();
          },
          error: (error: ErrorApiResponse) => {
            console.log(error);
            this.toastr.error(error.error, 'Error al realizar la reserva!!!')
          }
        }
      );
    } catch (error: Error | any) {
      this.toastr.error(error.message, 'Error al procesar la reserva');
    }
  }

  private limpiarCampos() {
    //Campos de la tarjeta
    this.cardInfo.name = ''
    this.cardInfo.number = ''
    this.cardInfo.fecha = ''
    this.cardInfo.cvv = ''
  }

  private validateCardInfo(cardInfo: any): void {
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

}
