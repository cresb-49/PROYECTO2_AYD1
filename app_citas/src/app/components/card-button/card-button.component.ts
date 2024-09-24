import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [RouterModule, CommonModule],
  selector: 'app-card-button',
  templateUrl: './card-button.component.html',
  styleUrls: ['./card-button.component.css']
})
export class CardButtonComponent implements OnInit {
  @Input() title: string = 'Noteworthy technology acquisitions 2021';
  @Input() subtitle: string = 'Subtitle';
  @Input() ruta: string = '/home';
  @Input() description: string = 'Here are the biggest enterprise technology acquisitions of 2021 so far, in reverse chronological order.';
  constructor() { }
  ngOnInit() {
  }
}
