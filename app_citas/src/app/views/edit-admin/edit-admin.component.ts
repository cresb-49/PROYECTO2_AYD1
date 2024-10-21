import { Component, OnInit } from '@angular/core';
import { CuAdminComponent } from '../../components/cu-admin/cu-admin.component';

@Component({
  standalone: true,
  imports: [CuAdminComponent],
  selector: 'app-edit-admin',
  templateUrl: './edit-admin.component.html',
  styleUrls: ['./edit-admin.component.css']
})
export class EditAdminComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
