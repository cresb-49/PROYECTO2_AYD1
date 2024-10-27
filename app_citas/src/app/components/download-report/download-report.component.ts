import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteService } from '../../services/reporte/reporte.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth/auth.service';
import { ErrorApiResponse } from '../../services/http/http.service';


export enum ExportType {
  pdf = 'pdf',
  excel = 'excel',
  word = 'word',
  img = 'img'
}

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-download-report',
  templateUrl: './download-report.component.html',
  styleUrls: ['./download-report.component.css']
})
export class DownloadReportComponent implements OnInit {
  ExportType = ExportType;

  @Input() title = 'Descargar Reporte';
  @Input() description = 'Seleccione el rango de fechas para descargar el reporte';
  @Input() report_type = '';
  init_date = '';
  end_date = '';
  constructor(
    private authService: AuthService,
    private reporteService: ReporteService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    //Validamos que el usuario este autenticado y no sea un cliente y lo redirigimos al "/"
    if (!this.authService.isLoggedIn()) {
      this.toastr.error('No tienes permisos para acceder a esta sección');
      window.location.href = '/';
    }
  }

  downloadReport(report_type: string, export_type: ExportType) {
    try {
      //Validamos que las fechas no esten vacias
      if (!this.init_date || !this.end_date) {
        throw new Error('Por favor seleccione un rango de fechas');
      }
      //Validamos que la fecha de inicio sea menor o igual a la fecha final
      //descomoponemos las fechas por split "-" y las convertimos a Date
      const year_init = parseInt(this.init_date.split('-')[0]);
      const month_init = parseInt(this.init_date.split('-')[1]);
      const day_init = parseInt(this.init_date.split('-')[2]);
      const year_end = parseInt(this.end_date.split('-')[0]);
      const month_end = parseInt(this.end_date.split('-')[1]);
      const day_end = parseInt(this.end_date.split('-')[2]);
      const init_date = new Date(year_init, month_init, day_init);
      const end_date = new Date(year_end, month_end, day_end);
      //si la fecha de inicio es mayor a la fecha final lanzamos un error
      if (init_date > end_date) {
        throw new Error('La fecha de inicio no puede ser mayor a la fecha final');
      }
      //si no hay errores descargamos el reporte
      this.reporteService.downloadReport(report_type, export_type, this.init_date, this.end_date).subscribe({
        next: (response: Blob | any) => {
          const url = window.URL.createObjectURL(response);
          window.open(url);
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al descargar el reporte');
        }
      });
    } catch (error: Error | any) {
      this.toastr.error(error.message, 'Error al descargar el reporte');
    }
  }
}
