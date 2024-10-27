import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { ReservaService } from '../../services/reserva/reserva.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';
@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-procesar-cita',
  templateUrl: './procesar-cita.component.html',
  styleUrls: ['./procesar-cita.component.css']
})
export class ProcesarCitaComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private reservaService: ReservaService,
    private toastr: ToastrService,
    private route: ActivatedRoute
  ) { }

  async ngOnInit() {
    this.cargarDatosRerserva();
  }

  cargarDatosRerserva() {
    //Obtenemos el id de la reserva en la url de la pagina
    const id_reserva = this.route.snapshot.paramMap.get('id');
    console.log("id_reserva", id_reserva);
  }

}
