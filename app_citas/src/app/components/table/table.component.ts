import { Component, Input, input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';


export interface TableHeader {
  name: string;
  key: string;
  main?: boolean;
}

export interface TableAction {
  name: string;
  icon: string;
  route?: string;
  action?: (value: any) => void;
  return?: boolean;
  key?: string;
}

@Component({
  standalone: true,
  imports: [RouterModule, CommonModule],
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {

  @Input() useDataModel: boolean = false;

  @Input() headers: TableHeader[] = [
    { name: 'Product Name', key: 'name', main: true },
    { name: 'Color', key: 'color' },
    { name: 'Category', key: 'category' },
    { name: 'Price', key: 'price' },
  ];

  @Input() data: any[] = [
    { name: 'Product 1', color: 'red', category: 'category 1', price: 100 },
    { name: 'Product 2', color: 'blue', category: 'category 2', price: 200 },
    { name: 'Product 3', color: 'green', category: 'category 3', price: 300 },
    { name: 'Product 4', color: 'yellow', category: 'category 4', price: 400 },
    { name: 'Product 5', color: 'purple', category: 'category 5', price: 500 }
  ]

  @Input() actions: TableAction[] = [
    { name: 'Edit', icon: 'edit', route: '/edit-empleado', key: 'name' },
    { name: 'Delete', icon: 'delete', action: this.helloOnTable, return: true },
  ]

  constructor() { }

  public helloOnTable(value: any): void {
    if (value) {
      alert('Hello on table' + value);
      console.log(value);
    } else {
      alert('Hello on table');
    }
  }

  ngOnInit() {
    if (this.useDataModel === false) {
      this.headers = [];
      this.data = [];
      this.actions = [];
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

  executeAction(action: any, value: any): void {
    if (!action.route) {
      if (action.action) {
        action.action(action.return ? value : null);
      }
    }
  }
}
