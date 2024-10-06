import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SetNewPasswordComponent } from '../../components/set-new-password/set-new-password.component';
@Component({
  standalone: true,
  imports: [RouterModule, CommonModule, SetNewPasswordComponent],
  selector: 'app-recovery-password',
  templateUrl: './recovery-password.component.html',
  styleUrls: ['./recovery-password.component.css']
})
export class RecoveryPasswordComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
