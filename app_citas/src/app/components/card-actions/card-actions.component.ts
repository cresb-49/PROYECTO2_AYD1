import { Component, Input, OnInit } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-card-actions',
  templateUrl: './card-actions.component.html',
  styleUrls: ['./card-actions.component.css']
})
export class CardActionsComponent implements OnInit {
  @Input() src = '';
  @Input() alt = '';
  @Input() title = '';
  @Input() description = '';
  constructor() { }

  ngOnInit() {
  }

}
