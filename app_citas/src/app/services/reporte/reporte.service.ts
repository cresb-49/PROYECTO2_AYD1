import { Injectable } from '@angular/core';
import { HttpService } from '../http/http.service';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {

  constructor(
    private httpService: HttpService
  ) { }

  downloadReport(report_type: string, export_type: string, init_date: string, end_date: string) {
    return this.httpService.post(`reporte/private/restricted/exportarReporte`, { tipoReporte: report_type, tipoExporte: export_type, fecha1: init_date, fecha2: end_date }, true, 'blob');
  }
}
