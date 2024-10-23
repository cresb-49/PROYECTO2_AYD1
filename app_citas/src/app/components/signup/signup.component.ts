import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { signUpCliente, UserService } from '../../services/user/user.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
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
  sendTokenForm: FormGroup;

  constructor(
    private toastr: ToastrService,
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private authService: AuthService
  ) {
    this.sendTokenForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
    this.signupForm = this.fb.group({
      nombres: ['', [Validators.required]],
      apellidos: ['', [Validators.required]],
      cui: ['', [Validators.required]],
      nit: ['', [Validators.required]],
      telefono: ['', [Validators.required]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
      tokenAuth: ['', [Validators.required]]
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

  onSubmitEmail() {
    if (this.sendTokenForm.valid) {
      this.userService.sendEmailVerificacion(this.sendTokenForm.value.email).subscribe(
        {
          next: (response: ApiResponse) => {
            this.toastr.success('Verifica tu bandeja de entrada', 'Email enviado con exito!!!');
            this.sendTokenForm.reset();
          },
          error: (error: ErrorApiResponse) => {
            this.toastr.error(error.error, 'Error al enviar el email')
          }
        }
      )
    }
  }
  onSubmit() {
    if (this.signupForm.valid) {
      console.log(this.signupForm.value);
      const payload: signUpCliente = {
        nombres: this.signupForm.value.nombres,
        apellidos: this.signupForm.value.apellidos,
        tokenAuth: this.signupForm.value.tokenAuth,
        cui: this.signupForm.value.cui,
        nit: this.signupForm.value.nit,
        phone: this.signupForm.value.telefono,
        password: this.signupForm.value.password
      };
      this.userService.signUpCliente(payload).subscribe(
        {
          next: (data: ApiResponse) => {
            this.toastr.success(data.message, "Registro exitoso!!!")
            this.clearForm();
          },
          error: (error: ErrorApiResponse) => {
            this.toastr.error(error.error, 'Error en el registro');
          }
        }
      );
    } else {
      this.toastr.error('Formulario inválido', 'Error en el registro');
    }
  }
}
