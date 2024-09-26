import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface EventConfig {
  title: string;
  interval: string;
}


@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-month-day-calendar',
  templateUrl: './month-day-calendar.component.html',
  styleUrls: ['./month-day-calendar.component.css']
})
export class MonthDayCalendarComponent implements OnInit {
  @Input() isMonthDay: boolean = true;
  @Input() dayNumber: number = 1;
  @Input() showEvents: boolean = true;
  @Input() events: EventConfig[] = [
    {
      title: 'Event 1',
      interval: '10:00 - 11:00'
    },
    {
      title: 'Event 2',
      interval: '11:00 - 12:00'
    }
  ];

  constructor() { }

  dayHasEvents(): boolean {
    return this.events.length > 0;
  }

  ngOnInit() {
  }

}
