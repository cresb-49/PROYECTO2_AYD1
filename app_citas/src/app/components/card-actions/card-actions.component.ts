import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

export interface CardAction {
  description: string;
  route?: string;
  key?: string;
  action?: (value: any) => void;
  enabled: boolean;
}
@Component({
  standalone: true,
  imports: [CommonModule, RouterModule],
  selector: 'app-card-actions',
  templateUrl: './card-actions.component.html',
  styleUrls: ['./card-actions.component.css']
})
export class CardActionsComponent implements OnInit {
  @Input() data: any = { id: 0 };

  @Input() src = '';
  @Input() alt = '';
  @Input() title = '';
  @Input() description = '';

  @Input() actions: CardAction[] = [
    { description: 'Action 1', route: '/action1', enabled: true },
    { description: 'Action 1', route: '/action1', key: 'id', enabled: true },
    { description: 'Action 2', action: this.helloOnCard, enabled: true },
    { description: 'Action 3', action: this.helloOnCard, enabled: false },
  ]
  constructor() { }

  ngOnInit() {
  }

  helloOnCard(data: any): void {
    alert('Hello on card: ' + data);
  }

  getEnabledActions() {
    return this.actions.filter(action => action.enabled);
  }

  executeAction(action: any, value: any): void {
    if (!action.route) {
      if (action.action) {
        action.action(value);
      }
    }
  }

  public getValueByKey(key: string, value: any): any {
    // La informacion puede venir name o product.name, donde cada punto es un nivel mas profundo
    const keys = key.split('.');
    let result = value;
    for (let i = 0; i < keys.length; i++) {
      result = result[keys[i]];
    }
    return result;
  }

}
