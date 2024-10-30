import { Component, OnInit } from '@angular/core';
import { Chart, ChartType } from 'chart.js/auto';
import { CommonModule } from '@angular/common';
import { Form, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ReporteService } from '../../services/reporte/reporte.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';

interface ClienteFrecuente {
  id: number;
  nombre: string;
  numeroReservaciones: number;
  valorTotalCompras: number;
  ticketPromedio: number;
  numeroPedidos: number;
}

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-graficos',
  templateUrl: './graficos.component.html',
  styleUrls: ['./graficos.component.css']
})
export class GraficosComponent implements OnInit {

  public charts: Map<string, Chart> = new Map<string, Chart>();
  public datesRange: Map<string, FormGroup> = new Map<string, FormGroup>();

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private reporteService: ReporteService
  ) {
    this.datesRange.set('demandChart', this.fb.group({
      start: ['', Validators.required],
      end: ['', Validators.required]
    }));
    this.datesRange.set('clientesFrecuentesChart', this.fb.group({
      start: ['', Validators.required],
      end: ['', Validators.required]
    }));
  }

  ngOnInit() {

    // const data = {
    //   labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    //   datasets: [
    //     {
    //       label: 'My First dataset',
    //       data: [0, 10, 5, 2, 20, 30, 45],
    //       fill: false,
    //       borderColor: 'rgb(75, 192, 192)',
    //       tension: 0.1
    //     }
    //   ]
    // };
    this.createDemandChart(true);
    this.createClientesFrecuentesChart(true);
  }

  createDemandChart(OnInit = false) {
    let servicios: { nombre: string, cantidadReservas: number }[] = [];
    let canchas: { nombre: string, cantidadReservas: number }[] = [];
    if (this.datesRange.get('demandChart')?.valid) {
      const start = this.datesRange.get('demandChart')?.get('start')?.value;
      const end = this.datesRange.get('demandChart')?.get('end')?.value;
      this.reporteService.serviciosMasSolicitados(start, end).subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          servicios = data.servicios;
          canchas = data.canchas;
          this.makeDemandChart(servicios, canchas);
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener los datos');
          this.makeDemandChart([], []);
        }
      }
      );
    } else {
      if (!OnInit) {
        this.toastr.error('Por favor, seleccione un rango de fechas válido', 'Error');
      }
      this.makeDemandChart([], []);
    }
  }

  makeDemandChart(servicios: { nombre: string, cantidadReservas: number }[], canchas: { nombre: string, cantidadReservas: number }[]) {
    const labels = [...servicios.map(s => s.nombre), ...canchas.map(c => c.nombre)];
    const dataServicios = servicios.map(s => s.cantidadReservas);
    const dataCanchas = canchas.map(c => c.cantidadReservas);

    const data = {
      labels: labels,
      datasets: [
        {
          label: 'Servicios',
          data: dataServicios,
          backgroundColor: 'rgba(75, 192, 192, 0.6)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        },
        {
          label: 'Canchas',
          data: dataCanchas,
          backgroundColor: 'rgba(153, 102, 255, 0.6)',
          borderColor: 'rgba(153, 102, 255, 1)',
          borderWidth: 1
        }
      ]
    };

    //Verificar si el chart ya existe
    if (this.charts.has('demandChart')) {
      this.charts.get('demandChart')?.destroy();
    }
    const chart = new Chart('demandChart', {
      type: 'bar' as ChartType,
      data: data,
      options: {
        responsive: true,
      }
    });
    this.charts.set('demandChart', chart);
  }

  createClientesFrecuentesChart(OnInit = false) {
    if (this.datesRange.get('clientesFrecuentesChart')?.valid) {
      const start = this.datesRange.get('clientesFrecuentesChart')?.get('start')?.value;
      const end = this.datesRange.get('clientesFrecuentesChart')?.get('end')?.value;
      this.reporteService.clientesFrecuentes(start, end).subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          this.makeClientesFrecuentesChart(data.clienteFrecuentes);
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener los datos');
          this.makeClientesFrecuentesChart([]);
        }
      });

    } else {
      if (!OnInit) {
        this.toastr.error('Por favor, seleccione un rango de fechas válido', 'Error');
      }
      this.makeClientesFrecuentesChart([]);
    }
  }

  makeClientesFrecuentesChart(clienteFrecuentes: ClienteFrecuente[]) {
    const labels = clienteFrecuentes.map(cliente => cliente.nombre);
    const dataReservaciones = clienteFrecuentes.map(cliente => cliente.numeroReservaciones);
    const data = {
      labels: labels,
      datasets: [
        {
          label: 'Número de Reservaciones',
          data: dataReservaciones,
          backgroundColor: 'rgba(75, 192, 192, 0.6)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }
      ]
    };
    //Verificar si el chart ya existe
    if (this.charts.has('clientesFrecuentesChart')) {
      this.charts.get('clientesFrecuentesChart')?.destroy();
    }
    const chart = new Chart('clientesFrecuentesChart', {
      type: 'bar',
      data: data,
      options: {
        responsive: true,
      }
    });
    this.charts.set('clientesFrecuentesChart', chart);
  }


}
