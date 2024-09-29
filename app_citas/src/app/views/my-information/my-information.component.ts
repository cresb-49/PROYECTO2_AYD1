import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';
@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-my-information',
  templateUrl: './my-information.component.html',
  styleUrls: ['./my-information.component.css']
})
export class MyInformationComponent implements OnInit {

  activeButtonSave = false;

  constructor() { }

  ngOnInit() {
  }

}
