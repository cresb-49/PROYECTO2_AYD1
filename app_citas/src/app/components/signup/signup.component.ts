import { Component, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { signUpCliente, UserService } from '../../services/user/user.service';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  imports: [ReactiveFormsModule, CommonModule],
  encapsulation: ViewEncapsulation.None
})
export class SignupComponent {
  signupForm: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService) {
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
