import { Component, OnInit } from '@angular/core';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { CommonModule } from '@angular/common';
import { CreateUserComponent } from '../../components/create-user/create-user.component';

@Component({
  standalone: true,
  imports: [ScheduleConfComponent, CommonModule, CreateUserComponent],
  selector: 'app-edit-employee',
  templateUrl: './edit-employee.component.html',
  styleUrls: ['./edit-employee.component.css']
})
export class EditEmployeeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
