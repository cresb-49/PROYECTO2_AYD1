import { Component, OnInit } from '@angular/core';
import { DownloadReportComponent } from '../../components/download-report/download-report.component';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule, DownloadReportComponent],
  selector: 'app-reportes',
  templateUrl: './reportes.component.html',
  styleUrls: ['./reportes.component.css']
})
export class ReportesComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
