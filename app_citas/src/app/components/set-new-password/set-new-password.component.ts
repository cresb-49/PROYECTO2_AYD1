import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RecoveryPasswordSend, UserService } from '../../services/user/user.service';
import { RouterModule } from '@angular/router';
import { AbstractControlOptions, Form, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  selector: 'app-set-new-password',
  templateUrl: './set-new-password.component.html',
  styleUrls: ['./set-new-password.component.css']
})
export class SetNewPasswordComponent implements OnInit {
  passwordForm: FormGroup;
  activeSendNewPassword = false;
  private changePaswordToken: string = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.passwordForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirm_password: ['', [Validators.required, Validators.minLength(6)]],
    }, {
      validators: this.passwordMatchValidator
    } as AbstractControlOptions);
  }


  ngOnInit() {
    this.passwordForm.statusChanges.subscribe(status => {
      this.activeSendNewPassword = status === 'VALID';
    });
    //Obtenermos el valor del parametro c en la url
    const urlParams = new URLSearchParams(window.location.search);
    const c = urlParams.get('c');
    this.changePaswordToken = c || '';
    console.log(this.changePaswordToken);
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

  sendNesPassword() {
    if (this.passwordForm.valid) {
      const payload: RecoveryPasswordSend = {
        nuevaPassword: this.passwordForm.get('password')?.value,
        codigo: this.changePaswordToken
      }
      this.userService.sendNewPassword(payload);
    }
  }
}
