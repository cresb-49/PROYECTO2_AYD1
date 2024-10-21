import { Component, Input, OnInit, resolveForwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DayConfig, ScheduleConfComponent } from '../schedule-conf/schedule-conf.component';
import { EmpleadoUpdateCreate, HorarioEmpleado, Rol, signUpCliente, UpdateInfoUser, UpdateUserPassword, UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, debounce, debounceTime, filter } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Dia, DiaService } from '../../services/dia/dia.service';
import { ActivatedRoute } from '@angular/router';

export interface CreateUpdateCliente {
  id?: number;
  nombres: string;
  apellidos: string;
  email: string;
  phone: string;
  password: string;
  current_password?: string;
  confirm_password?: string;
  cui: string;
  nit: string;
}

export interface UpdatePassword {
  password: string;
  current_password: string;
  confirm_password: string;
}

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ScheduleConfComponent, FormsModule],
  selector: 'app-cu-cliente',
  templateUrl: './cu-cliente.component.html',
  styleUrls: ['./cu-cliente.component.css']
})
export class CuClienteComponent implements OnInit {
  @Input() confWorkSchedule = false;
  @Input() isCliente = false;
  @Input() modificar = false;

  activeButtonSave = false;
  activeChangePassword = false;

  roles: Rol[] = [];
  dataDias: Dia[] = []

  changePassword: UpdatePassword = {
    password: '',
    confirm_password: '',
    current_password: ''
  }

  cliente: CreateUpdateCliente = {
    nombres: '',
    apellidos: '',
    email: '',
    nit: '',
    phone: '',
    password: '',
    confirm_password: '',
    cui: ''
  }

  clienteOriginal: CreateUpdateCliente = {
    nombres: '',
    apellidos: '',
    email: '',
    nit: '',
    phone: '',
    password: '',
    confirm_password: '',
    cui: ''
  }

  private adminDataSubject = new BehaviorSubject<CreateUpdateCliente>(this.cliente); // Para observar cambios
  private changePassworSubject = new BehaviorSubject<UpdatePassword>(this.changePassword)


  constructor(
    private toastr: ToastrService,
    private userService: UserService,
    private route: ActivatedRoute
  ) { }

  async ngOnInit() {
    if (this.modificar) {
      await this.cargarDatosCliente();
    }
    this.adminDataSubject.pipe(
      debounceTime(300) // Evitar múltiples comparaciones inmediatas
    ).subscribe(newData => {
      if (this.modificar) {
        this.activeButtonSave = !this.compararObjetos(newData, this.clienteOriginal);
        if (this.activeButtonSave) {
          this.toastr.info('Hay cambios pendientes por guardar');
        }
      } else {
        this.activeButtonSave = true;
      }
    });
    this.changePassworSubject.pipe(
      debounceTime(300)
    ).subscribe(newData => {
      this.activeChangePassword = this.verificarChangePassword(newData);
    })
  }

  verificarChangePassword(data: UpdatePassword): boolean {
    //Si todos los cambpos estan vacios se retorna false, pero no se emite notificacion
    //Si esta definido el campo current_password, pero password y confirm estan vacion no se emite nada
    // Si password y confirm no son iguales se emite una alerta
    if (data.current_password.length <= 0 && data.password.length <= 0 && data.confirm_password.length <= 0) {
      return false;
    }
    if (data.current_password.length > 0 && data.password.length <= 0 && data.confirm_password.length <= 0) {
      return false;
    }
    if (data.password !== data.confirm_password) {
      this.toastr.error('Las contraseñas no coinciden');
      return false;
    } else {
      if (data.current_password.length <= 0) {
        this.toastr.error('Debe de ingresar la contraseña actual');
        return false;
      } else {
        return true;
      }
    }
  }

  changePasswordAction() {
    if (this.activeChangePassword) {
      const payload: UpdateUserPassword = {
        id: Number(this.cliente.id),
        password: this.changePassword.current_password,
        newPassword: this.changePassword.password
      };
      this.userService.changeUserPassword(payload).subscribe({
        next: (response: ApiResponse) => {
          this.toastr.success('Contraseña cambiada correctamente');
          this.changePassword = {
            password: '',
            confirm_password: '',
            current_password: ''
          }
          this.changePassworSubject.next(this.changePassword);
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al cambiar la contraseña');
        }
      });
    }
  }

  // Comparación profunda entre dos objetos
  compararObjetos(obj1: CreateUpdateCliente, obj2: CreateUpdateCliente): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  onNombresChange(newNombre: string) {
    this.cliente.nombres = newNombre;
    this.adminDataSubject.next(this.cliente);
  }

  onApellidosChange(newApellido: string) {
    this.cliente.apellidos = newApellido;
    this.adminDataSubject.next(this.cliente);
  }

  onEmailChange(newEmail: string) {
    this.cliente.email = newEmail;
    this.adminDataSubject.next(this.cliente);
  }

  onPhoneChange(newPhone: string) {
    this.cliente.phone = newPhone;
    this.adminDataSubject.next(this.cliente);
  }

  onCuiChange(newCui: string) {
    this.cliente.cui = newCui;
    this.adminDataSubject.next(this.cliente);
  }

  onNitChange(newNit: string) {
    this.cliente.nit = newNit;
    this.adminDataSubject.next(this.cliente);
  }

  onPasswordChange(newPassword: string) {
    this.cliente.password = newPassword;
    this.adminDataSubject.next(this.cliente);
    this.compararPassword();
  }

  onConfirmPasswordChange(newPassword: string) {
    this.cliente.confirm_password = newPassword;
    this.adminDataSubject.next(this.cliente);
    this.compararPassword();
  }

  onUpdatePasswordChange(pass: string) {
    this.changePassword.password = pass;
    this.changePassworSubject.next(this.changePassword);
  }

  onUpdateConfirmPasswordChange(pass: string) {
    this.changePassword.confirm_password = pass;
    this.changePassworSubject.next(this.changePassword);
  }

  onUpdateCurrentPasswordChange(pass: string) {
    this.changePassword.current_password = pass;
    this.changePassworSubject.next(this.changePassword);
  }

  compararPassword(): boolean {
    //Verificamos si las password estan vacias
    if ((this.cliente.password ?? '').length <= 0) {
      this.toastr.error('Debe de asignar una contraseña')
      return false;
    }
    if (this.cliente.password !== this.cliente.confirm_password) {
      this.toastr.error('Las contraseñas no coinciden');
      return false;
    }
    return true;
  }

  accionBoton() {
    if (this.modificar) {
      this.modificarCliente()
    } else {
      this.crearCliente()
    }
  }


  crearCliente() {
    if (this.compararPassword()) {
      const dataPayload: signUpCliente = {
        nombres: this.cliente.nombres,
        apellidos: this.cliente.apellidos,
        email: this.cliente.email,
        password: this.cliente.password,
        phone: this.cliente.phone,
        nit: this.cliente.nit,
        cui: this.cliente.cui
      }
      this.userService.signUpCliente(dataPayload).subscribe({
        next: (response: ApiResponse) => {
          this.toastr.success('Cliente creado correctamente');
          //Limpiamos los campos
          this.cliente = {
            nombres: '',
            apellidos: '',
            email: '',
            nit: '',
            phone: '',
            password: '',
            confirm_password: '',
            cui: ''
          }
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al crear el cliente')
        }
      });
    }
  }

  modificarCliente() {

    const dataPayload: UpdateInfoUser = {
      id: Number(this.cliente.id),
      nombres: this.cliente.nombres,
      apellidos: this.cliente.apellidos,
      email: this.cliente.email,
      phone: this.cliente.phone,
      nit: this.cliente.nit,
      cui: this.cliente.cui
    }
    this.userService.changeUserInfo(dataPayload).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success('Cliente modificado correctamente');
        this.clienteOriginal.nombres = this.cliente.nombres;
        this.clienteOriginal.apellidos = this.cliente.apellidos;
        this.clienteOriginal.email = this.cliente.email;
        this.clienteOriginal.phone = this.cliente.phone;
        this.clienteOriginal.nit = this.cliente.nit;
        this.clienteOriginal.cui = this.cliente.cui;
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al modificar el cliente')
      }
    });
  }

  cargarDatosCliente() {
    const id_admin = this.route.snapshot.paramMap.get('id');
    this.userService.getUser(Number(id_admin)).subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        this.cliente.id = data.id;
        this.cliente.nombres = data.nombres;
        this.cliente.apellidos = data.apellidos;
        this.cliente.email = data.email;
        this.cliente.phone = data.phone;
        this.cliente.nit = data.nit;
        this.cliente.cui = data.cui;
        //Ahora una copia de los datos originales
        this.clienteOriginal.id = data.id;
        this.clienteOriginal.nombres = data.nombres;
        this.clienteOriginal.apellidos = data.apellidos;
        this.clienteOriginal.email = data.email;
        this.clienteOriginal.phone = data.phone;
        this.clienteOriginal.nit = data.nit;
        this.clienteOriginal.cui = data.cui;
        this.adminDataSubject.next(this.cliente);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener el cliente');
      }
    });
  }
}
