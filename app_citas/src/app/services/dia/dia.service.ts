import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpService } from '../http/http.service';
import { ToastrService } from 'ngx-toastr';

export interface Dia {
  id: number;
  nombre: string;
}

@Injectable({
  providedIn: 'root'
})
export class DiaService {

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

  getDias() {
    return this.httpService.get<any>('dia/public/dias');
  }

}
