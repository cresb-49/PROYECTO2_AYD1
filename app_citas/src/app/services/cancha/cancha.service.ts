import { Injectable } from '@angular/core';
import { HttpService } from '../http/http.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

// "id": 39,
// 			"createdAt": "2024-10-07T01:43:15",
// 			"costoHora": 100.0,
// 			"descripcion": "Cancha de futbol 5",
// 			"horarios": [

export interface Cancha {
  id: number;
  costoHora: number;
  descripcion: string;
  horarios: any[];
}


@Injectable({
  providedIn: 'root'
})
export class CanchaService {

  constructor(
    private router: Router,
    private httpService: HttpService,
    private toastr: ToastrService
  ) { }

  getCanchas() {
    return this.httpService.get<any>('cancha/public/canchas')
  }

  getCancha(id: number) {
    return this.httpService.get<any>(`cancha/public/cancha/${id}`)
  }

}
