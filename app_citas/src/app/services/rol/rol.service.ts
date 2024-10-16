import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class RolService {

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

  getRolById(id: number) {
    return this.httpService.get<any>(`rol/private/no_restricted/getRolById/${id}`, null, true);
  }

  getPermisos() {
    return this.httpService.get<any>('permiso/private/restricted/permisos', null, true);
  }

}
