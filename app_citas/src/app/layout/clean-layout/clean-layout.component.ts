import { Component} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  standalone: true,
  imports: [
    NavbarComponent,
    RouterOutlet
  ],
  selector: 'app-clean-layout',
  templateUrl: './clean-layout.component.html',
  styleUrls: ['./clean-layout.component.css']
})
export class CleanLayoutComponent {
  constructor() { }
}
