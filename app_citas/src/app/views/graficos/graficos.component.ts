import { Component, OnInit } from '@angular/core';
import { Chart, ChartType } from 'chart.js/auto';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-graficos',
  templateUrl: './graficos.component.html',
  styleUrls: ['./graficos.component.css']
})
export class GraficosComponent implements OnInit {

  public chart: Chart | null = null;
  public chart2: Chart | null = null;

  constructor() { }

  ngOnInit() {

    const data = {
      labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
      datasets: [
        {
          label: 'My First dataset',
          data: [0, 10, 5, 2, 20, 30, 45],
          fill: false,
          borderColor: 'rgb(75, 192, 192)',
          tension: 0.1
        }
      ]
    };
    this.chart = new Chart(
      "chart", {
      type: 'line' as ChartType,
      data
    }
    ); this.chart2 = new Chart(
      "chart2", {
      type: 'line' as ChartType,
      data
    }
    );
  }

}
