import { Component, OnInit } from '@angular/core';
import { CalendarComponent } from '../../components/calendar-components/calendar/calendar.component';
import { CommonModule } from '@angular/common';
import { EventConfig } from '../../components/calendar-components/month-day-calendar/month-day-calendar.component';
import { Input } from '@angular/core';
import { MonthDayCalendarComponent } from '../../components/calendar-components/month-day-calendar/month-day-calendar.component';
@Component({
  standalone: true,
  imports: [CalendarComponent, CommonModule, MonthDayCalendarComponent],
  selector: 'app-my-calendar',
  templateUrl: './my-calendar.component.html',
  styleUrls: ['./my-calendar.component.css']
})
export class MyCalendarComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
