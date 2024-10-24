import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface EventConfig {
  title: string;
  interval: string;
  isReserva: boolean;
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
      interval: '10:00 - 11:00',
      isReserva: false
    },
    {
      title: 'Event 2',
      interval: '11:00 - 12:00',
      isReserva: true
    }
  ];

  // Control de la visibilidad y contenido del popover
  isPopoverVisible: boolean = false;
  activePopover: 'reservations' | 'appointments' | null = null;
  popoverPosition = { top: '0px', left: '0px' };

  constructor() { }

  ngOnInit() {
  }

  dayHasEvents(): boolean {
    return this.events.length > 0;
  }

  showPopover(type: 'reservations' | 'appointments'): void {
    this.isPopoverVisible = true;
    this.activePopover = type;
    this.setPosition(); // Actualiza la posición del popover basado en el punto
  }

  hidePopover(): void {
    this.isPopoverVisible = false;
    this.activePopover = null;
  }

  private setPosition(): void {
    // Calcula la posición donde el popover debe aparecer
    this.popoverPosition = {
      top: '-20px', // Ajusta la posición vertical del popover
      left: '-40px' // Ajusta la posición horizontal del popover
    };
  }
}
