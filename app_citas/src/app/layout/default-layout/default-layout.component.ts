import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { RouterOutlet } from '@angular/router';
@Component({
  standalone: true,
  selector: 'app-default-layout',
  imports: [NavbarComponent, CommonModule, RouterOutlet],
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.css']
})
export class DefaultLayoutComponent {
  constructor() { }
}
