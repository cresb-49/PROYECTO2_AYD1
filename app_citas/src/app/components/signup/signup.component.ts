import { Component ,ViewEncapsulation} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon'; // Para íconos como el de visibilidad
import { NgIf } from '@angular/common'; // Necesario para las directivas de control como *ngIf


@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    NgIf // Asegúrate de importar también las directivas de Angular
  ],
  encapsulation: ViewEncapsulation.None
})
export class SignupComponent {
  loginForm: FormGroup;
  hide = true;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  getErrorMessage(field: string): string {
    const control = this.loginForm.get(field);
    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }
    if (control?.hasError('email')) {
      return 'Correo inválido';
    }
    if (control?.hasError('minlength')) {
      return 'Debe tener al menos 6 caracteres';
    }
    return '';
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      console.log('Email:', email);
      console.log('Password:', password);
      // Aquí puedes hacer el envío del formulario al backend para autenticar al usuario
    }
  }
}
