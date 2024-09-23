import { Component, OnInit } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';
@Component({
  standalone: true,
  imports: [TableComponent],
  selector: 'app-see-employees',
  templateUrl: './see-employees.component.html',
  styleUrls: ['./see-employees.component.css']
})
export class SeeEmployeesComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
