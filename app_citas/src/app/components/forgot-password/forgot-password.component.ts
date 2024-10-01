import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { ReactiveFormsModule } from '@angular/forms';
@Component({
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, ReactiveFormsModule],
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  emailForm: FormGroup;
  activeSendEmail = false;

  constructor(private userService: UserService, private fb: FormBuilder) {
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit() {
    this.emailForm.statusChanges.subscribe(status => {
      this.activeSendEmail = status === 'VALID';
    });
  }

  sendEmail() {
    if (this.emailForm.valid) {
      this.userService.sendRecoveryEmail(this.emailForm.get('email')?.value);
    }
  }

}
