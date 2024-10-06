import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { signUpCliente, UserService } from '../../services/user/user.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  imports: [ReactiveFormsModule, CommonModule],
  encapsulation: ViewEncapsulation.None
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router, private authService: AuthService) {
    this.signupForm = this.fb.group({
      nombres: ['', [Validators.required]],
      apellidos: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      cui: ['', [Validators.required]],
      nit: ['', [Validators.required]],
      telefono: ['', [Validators.required]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    //Si el usuario ya está autenticado, redirigirlo a la página de inicio
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  clearForm() {
    this.signupForm.reset();
  }

  onSubmit() {
    if (this.signupForm.valid) {
      console.log(this.signupForm.value);
      const payload: signUpCliente = {
        nombres: this.signupForm.value.nombres,
        apellidos: this.signupForm.value.apellidos,
        email: this.signupForm.value.email,
        cui: this.signupForm.value.cui,
        nit: this.signupForm.value.nit,
        phone: this.signupForm.value.telefono,
        password: this.signupForm.value.password
      };
      this.userService.signUpCliente(payload).subscribe(
        {
          next: (data) => {
            console.log('response:', data);
            this.clearForm();
          },
          error: (error) => {
            console.error('Error:', error);
          }
        }
      );
    } else {
      console.error('Formulario inválido');
    }
  }
}
