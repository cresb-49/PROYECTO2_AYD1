import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
@Component({
  standalone: true,
  imports: [CommonModule, RouterModule],
  selector: 'app-card-actions',
  templateUrl: './card-actions.component.html',
  styleUrls: ['./card-actions.component.css']
})
export class CardActionsComponent implements OnInit {
  @Input() src = '';
  @Input() alt = '';
  @Input() title = '';
  @Input() description = '';
  @Input() actions = [
    { description: 'Action 1', route: '/action1', enabled: true },
    { description: 'Action 2', route: '/action2', enabled: false },
  ]
  constructor() { }

  ngOnInit() {
  }

  getEnabledActions() {
    return this.actions.filter(action => action.enabled);
  }

}
