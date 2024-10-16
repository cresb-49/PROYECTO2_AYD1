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

export interface CreateUpdateAdmin {
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
  selector: 'app-cu-admin',
  templateUrl: './cu-admin.component.html',
  styleUrls: ['./cu-admin.component.css']
})
export class CuAdminComponent implements OnInit {
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

  admin: CreateUpdateAdmin = {
    nombres: '',
    apellidos: '',
    email: '',
    nit: '',
    phone: '',
    password: '',
    confirm_password: '',
    cui: ''
  }

  adminOrignal: CreateUpdateAdmin = {
    nombres: '',
    apellidos: '',
    email: '',
    nit: '',
    phone: '',
    password: '',
    confirm_password: '',
    cui: ''
  }

  private adminDataSubject = new BehaviorSubject<CreateUpdateAdmin>(this.admin); // Para observar cambios
  private changePassworSubject = new BehaviorSubject<UpdatePassword>(this.changePassword)


  constructor(
    private toastr: ToastrService,
    private userService: UserService,
    private route: ActivatedRoute
  ) { }

  async ngOnInit() {
    if (this.modificar) {
      await this.cargarDatosAdmin();
    }
    this.adminDataSubject.pipe(
      debounceTime(300) // Evitar múltiples comparaciones inmediatas
    ).subscribe(newData => {
      if (this.modificar) {
        this.activeButtonSave = !this.compararObjetos(newData, this.adminOrignal);
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
        id: Number(this.admin.id),
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
  compararObjetos(obj1: CreateUpdateAdmin, obj2: CreateUpdateAdmin): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  onNombresChange(newNombre: string) {
    this.admin.nombres = newNombre;
    this.adminDataSubject.next(this.admin);
  }

  onApellidosChange(newApellido: string) {
    this.admin.apellidos = newApellido;
    this.adminDataSubject.next(this.admin);
  }

  onEmailChange(newEmail: string) {
    this.admin.email = newEmail;
    this.adminDataSubject.next(this.admin);
  }

  onPhoneChange(newPhone: string) {
    this.admin.phone = newPhone;
    this.adminDataSubject.next(this.admin);
  }

  onCuiChange(newCui: string) {
    this.admin.cui = newCui;
    this.adminDataSubject.next(this.admin);
  }

  onNitChange(newNit: string) {
    this.admin.nit = newNit;
    this.adminDataSubject.next(this.admin);
  }

  onPasswordChange(newPassword: string) {
    this.admin.password = newPassword;
    this.adminDataSubject.next(this.admin);
    this.compararPassword();
  }

  onConfirmPasswordChange(newPassword: string) {
    this.admin.confirm_password = newPassword;
    this.adminDataSubject.next(this.admin);
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
    if ((this.admin.password ?? '').length <= 0) {
      this.toastr.error('Debe de asignar una contraseña')
      return false;
    }
    if (this.admin.password !== this.admin.confirm_password) {
      this.toastr.error('Las contraseñas no coinciden');
      return false;
    }
    return true;
  }

  accionBoton() {
    if (this.modificar) {
      this.modificarAdmin()
    } else {
      this.crearAdmin()
    }
  }


  crearAdmin() {
    if (this.compararPassword()) {
      const dataPayload: signUpCliente = {
        nombres: this.admin.nombres,
        apellidos: this.admin.apellidos,
        email: this.admin.email,
        password: this.admin.password,
        phone: this.admin.phone,
        nit: this.admin.nit,
        cui: this.admin.cui
      }
      this.userService.crearAdmin(dataPayload).subscribe({
        next: (response: ApiResponse) => {
          this.toastr.success('Admin creado correctamente');
          //Limpiamos los campos
          this.admin = {
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
          this.toastr.error(error.error, 'Error al crear el admin')
        }
      });
    }
  }

  modificarAdmin() {

    const dataPayload: UpdateInfoUser = {
      id: Number(this.admin.id),
      nombres: this.admin.nombres,
      apellidos: this.admin.apellidos,
      email: this.admin.email,
      phone: this.admin.phone,
      nit: this.admin.nit,
      cui: this.admin.cui
    }
    this.userService.changeUserInfo(dataPayload).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success('Admin modificado correctamente');
        this.adminOrignal.nombres = this.admin.nombres;
        this.adminOrignal.apellidos = this.admin.apellidos;
        this.adminOrignal.email = this.admin.email;
        this.adminOrignal.phone = this.admin.phone;
        this.adminOrignal.nit = this.admin.nit;
        this.adminOrignal.cui = this.admin.cui;
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al modificar el admin')
      }
    });
  }

  cargarDatosAdmin() {
    const id_admin = this.route.snapshot.paramMap.get('id');
    this.userService.getUser(Number(id_admin)).subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        this.admin.id = data.id;
        this.admin.nombres = data.nombres;
        this.admin.apellidos = data.apellidos;
        this.admin.email = data.email;
        this.admin.phone = data.phone;
        this.admin.nit = data.nit;
        this.admin.cui = data.cui;
        //Ahora una copia de los datos originales
        this.adminOrignal.id = data.id;
        this.adminOrignal.nombres = data.nombres;
        this.adminOrignal.apellidos = data.apellidos;
        this.adminOrignal.email = data.email;
        this.adminOrignal.phone = data.phone;
        this.adminOrignal.nit = data.nit;
        this.adminOrignal.cui = data.cui;
        this.adminDataSubject.next(this.admin);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener el admin');
      }
    });
  }
}
