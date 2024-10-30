import { Component, OnInit } from '@angular/core';
import { Chart, ChartType } from 'chart.js/auto';
import { CommonModule } from '@angular/common';
import { Form, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ReporteService } from '../../services/reporte/reporte.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';

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

  servicios: { nombre: string, cantidadReservas: number }[] = [];
  canchas: { nombre: string, cantidadReservas: number }[] = [];

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private reporteService: ReporteService
  ) {
    this.datesRange.set('demandChart', this.fb.group({
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
  }

  createDemandChart(OnInit = false): void {
    if (this.datesRange.get('demandChart')?.valid) {
      const start = this.datesRange.get('demandChart')?.get('start')?.value;
      const end = this.datesRange.get('demandChart')?.get('end')?.value;
      this.reporteService.serviciosMasSolicitados(start, end).subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          console.log("Data", data);
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener los datos');
        }
      }
      );
    } else {
      if (!OnInit) {
        this.toastr.error('Por favor, seleccione un rango de fechas válido', 'Error');
        this.canchas = [];
        this.servicios = [];
      }
    }
    const labels = [...this.servicios.map(s => s.nombre), ...this.canchas.map(c => c.nombre)];
    const dataServicios = this.servicios.map(s => s.cantidadReservas);
    const dataCanchas = this.canchas.map(c => c.cantidadReservas);

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

    const config = {
      type: 'bar',
      data: data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true
          }
        },
        plugins: {
          legend: {
            position: 'top'
          },
          title: {
            display: true,
            text: 'Cantidad de Reservas por Servicio y Cancha'
          }
        }
      }
    };
    const chart = new Chart('demandChart', {
      type: 'bar' as ChartType,
      data: data,
      options: {
        responsive: true,
      }
    });
    this.charts.set('demandChart', chart);
  }
}
