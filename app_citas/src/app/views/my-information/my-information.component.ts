import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { UpdateInfoUser, UpdateUserPassword, UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';

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

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
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
      current_password: ['', [Validators.required, Validators.minLength(6)]],
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    // Escuchamos cambios en los formularios para habilitar los botones ademas los formularios tuvieron que ser modificados
    this.infoForm.statusChanges.subscribe(status => {
      this.activeButtonSave = status === 'VALID' && this.infoForm.dirty;
    });
    this.passwordForm.statusChanges.subscribe(status => {
      this.activeChangePassword = status === 'VALID' && this.passwordForm.dirty;
    });
    //Obtenemos la información del usuario
    this.getMyInformation();
  }

  getMyInformation() {
    this.userService.getPerfil(this.authService.getId()).subscribe({
      next: (data: ApiResponse) => {
        this.setInfoForm(data.data);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error');
      }
    });
  }

  private setInfoForm(data: any) {
    this.infoForm.setValue({
      first_name: data.nombres,
      last_name: data.apellidos,
      nit: data.nit,
      phone: data.phone,
      cui: data.cui,
      email: data.email,
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
      const payload: UpdateInfoUser = {
        id: this.authService.getId(),
        nombres: this.infoForm.get('first_name')?.value,
        apellidos: this.infoForm.get('last_name')?.value,
        email: this.infoForm.get('email')?.value,
        phone: this.infoForm.get('phone')?.value,
        nit: this.infoForm.get('nit')?.value,
        cui: this.infoForm.get('cui')?.value,
      };
      this.userService.changeUserInfo(payload).subscribe({
        next: (data: ApiResponse) => {
          this.toastr.success('Información actualizada', 'Éxito');
          this.setInfoForm(data.data);
          this.activeButtonSave = false;
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error');
        }
      });
    }
  }


  submitPasswordForm() {
    if (this.passwordForm.valid) {
      const payload: UpdateUserPassword = {
        id: this.authService.getId(),
        password: this.passwordForm.get('current_password')?.value,
        newPassword: this.passwordForm.get('password')?.value
      };
      this.userService.changeUserPassword(payload).subscribe({
        next: (data: ApiResponse) => {
          this.toastr.success('Contraseña actualizada', 'Éxito');
          this.passwordForm.reset();
          this.activeChangePassword = false;
          this.toastr.info('Por favor inicia sesión nuevamente', 'Información');
          this.authService.logout();
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error');
        }
      })
    }
  }
}
