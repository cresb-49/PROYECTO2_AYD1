import { Component, OnInit } from '@angular/core';
import { DownloadReportComponent } from '../../components/download-report/download-report.component';
import { CommonModule } from '@angular/common';

export interface ReporteView {
  titulo: string;
  descripcion: string;
  tipo: string;
}

@Component({
  standalone: true,
  imports: [CommonModule, DownloadReportComponent],
  selector: 'app-reportes',
  templateUrl: './reportes.component.html',
  styleUrls: ['./reportes.component.css']
})
export class ReportesComponent implements OnInit {

  reportes: ReporteView[] = [
    {
      titulo: 'Reporte de Ventas',
      descripcion: 'Seleccione el rango de fechas para descargar el reporte',
      tipo: 'reporteVentas',
    },
    {
      titulo: 'Reporte Clientes',
      descripcion: 'Seleccione el rango de fechas para descargar el reporte',
      tipo: 'reporteClientes'
    },
    {
      titulo: 'Reporte Servicios',
      descripcion: 'Seleccione el rango de fechas para descargar el reporte',
      tipo: 'reporteServicios'
    },{
      titulo: 'Reporte Disponibilidad',
      descripcion: 'Seleccione el rango de fechas para descargar el reporte',
      tipo: 'reporteDisponibilidad'
    }
  ];



  constructor() { }

  ngOnInit() {
  }

}
