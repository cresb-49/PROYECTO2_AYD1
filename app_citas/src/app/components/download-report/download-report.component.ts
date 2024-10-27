import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-download-report',
  templateUrl: './download-report.component.html',
  styleUrls: ['./download-report.component.css']
})
export class DownloadReportComponent implements OnInit {

  title = 'Descargar Reporte';
  description = 'Seleccione el rango de fechas para descargar el reporte';
  init_date = '';
  end_date = '';
  constructor() { }

  ngOnInit() {
  }

}
