import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ScheduleConfComponent } from '../schedule-conf/schedule-conf.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ScheduleConfComponent],
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {
  @Input() confWorkSchedule = false;
  activeButtonSave = false;
  constructor() { }

  ngOnInit() {
  }

}
