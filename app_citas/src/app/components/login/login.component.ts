import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { Router, RouterModule } from '@angular/router';


@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ]
})
export class LoginComponent implements OnInit {
  email = '';
  password = '';

  constructor(private router: Router, private toastr: ToastrService, private authService: AuthService) { }

  ngOnInit() {
    //Si el usuario ya está autenticado, redirigirlo a la página de inicio
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
  }


  sendLogin() {
    console.log('userName:', this.email);
    console.log('password:', this.password);
    let result = this.authService.login(this.email, this.password);
    console.log('result:', result);
  }

}
