import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpService } from '../http/http.service';

export interface signUpCliente {
  nombres: string;
  apellidos: string;
  email: string;
  password: string;
  telefono: string;
  nit: string;
  cui: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private router: Router, private httpService: HttpService) { }

  signUpCliente() {

  }
}
