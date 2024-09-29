import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-my-information',
  templateUrl: './my-information.component.html',
  styleUrls: ['./my-information.component.css']
})
export class MyInformationComponent implements OnInit {
  // Formularios reactivos
  infoForm: FormGroup;
  passwordForm: FormGroup;
  //
  activeButtonSave = false;
  activeChangePassword = false;

  constructor(private fb: FormBuilder) {
    // Inicializamos los formularios con FormBuilder
    this.infoForm = this.fb.group({
      first_name: ['', [Validators.required, Validators.minLength(2)]],
      last_name: ['', [Validators.required, Validators.minLength(2)]],
      nit: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]{8}$/)]],
      cui: ['', [Validators.required, Validators.pattern(/^[0-9]{13}$/)]],
      email: ['', [Validators.required, Validators.email]],
    });

    this.passwordForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirm_password: ['', [Validators.required, Validators.minLength(6)]],
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    // Escuchamos cambios en los formularios para habilitar los botones
    this.infoForm.statusChanges.subscribe(status => {
      this.activeButtonSave = status === 'VALID';
    });

    this.passwordForm.statusChanges.subscribe(status => {
      this.activeChangePassword = status === 'VALID';
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirm_password')?.value;

    // Si las contraseñas no coinciden, agregamos el error 'mismatch'
    if (password !== confirmPassword) {
      form.get('confirm_password')?.setErrors({ mismatch: true });
      return { mismatch: true };
    } else {
      // Si coinciden, eliminamos el error 'mismatch'
      if (form.get('confirm_password')?.hasError('mismatch')) {
        delete form.get('confirm_password')?.errors?.['mismatch'];
        form.get('confirm_password')?.updateValueAndValidity({ onlySelf: true });
      }
      return null;
    }
  }

  // Métodos para manejar los formularios
  submitInfoForm() {
    if (this.infoForm.valid) {
      console.log('Formulario Información:', this.infoForm.value);
    }
  }

  submitPasswordForm() {
    if (this.passwordForm.valid) {
      console.log('Formulario Contraseña:', this.passwordForm.value);
    }
  }
}
