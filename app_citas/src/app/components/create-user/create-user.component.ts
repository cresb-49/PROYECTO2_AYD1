import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DayConfig, ScheduleConfComponent } from '../schedule-conf/schedule-conf.component';
import { Rol, UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, debounceTime } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Dia, DiaService } from '../../services/dia/dia.service';

export interface CreateUpdateEmpleado {
  id_empleado?: number;
  id_usuario?: number;
  nombres: string;
  apellidos: string;
  email: string;
  phone: string;
  password: string;
  confirm_password?: string;
  cui: string;
  nit?: string;
  rol: number;
  horario: DayConfig[];
}

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ScheduleConfComponent, FormsModule],
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {
  @Input() confWorkSchedule = false;
  @Input() isCliente = false;
  @Input() modificar = false;

  activeButtonSave = false;
  roles: Rol[] = [];
  dataDias: Dia[] = []

  empleado: CreateUpdateEmpleado = {
    nombres: '',
    apellidos: '',
    email: '',
    phone: '',
    password: '',
    confirm_password: '',
    cui: '',
    rol: 0,
    horario: []
  }

  empleadoOrignal: CreateUpdateEmpleado = {
    nombres: '',
    apellidos: '',
    email: '',
    phone: '',
    password: '',
    confirm_password: '',
    cui: '',
    rol: 0,
    horario: []
  }

  private empleadoDataSubject = new BehaviorSubject<CreateUpdateEmpleado>(this.empleado); // Para observar cambios

  constructor(
    private toastr: ToastrService,
    private userService: UserService,
    private diaService: DiaService
  ) { }

  async ngOnInit() {
    await this.getRolesGenericos();
    await this.getDias();
    this.empleadoDataSubject.pipe(
      debounceTime(300) // Evitar múltiples comparaciones inmediatas
    ).subscribe(newData => {
      this.activeButtonSave = !this.compararObjetos(newData, this.empleadoOrignal);
      if (this.activeButtonSave && this.modificar) {
        this.toastr.info('Hay cambios pendientes por guardar');
      }
    });
  }

  // Comparación profunda entre dos objetos
  compararObjetos(obj1: CreateUpdateEmpleado, obj2: CreateUpdateEmpleado): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  onNombresChange(newNombre: string) {
    this.empleado.nombres = newNombre;
    this.empleadoDataSubject.next(this.empleado);
  }

  onApellidosChange(newApellido: string) {
    this.empleado.apellidos = newApellido;
    this.empleadoDataSubject.next(this.empleado);
  }

  onEmailChange(newEmail: string) {
    this.empleado.email = newEmail;
    this.empleadoDataSubject.next(this.empleado);
  }

  onPhoneChange(newPhone: string) {
    this.empleado.phone = newPhone;
    this.empleadoDataSubject.next(this.empleado);
  }

  onCuiChange(newCui: string) {
    this.empleado.cui = newCui;
    this.empleadoDataSubject.next(this.empleado);
  }

  onNitChange(newNit: string) {
    this.empleado.nit = newNit;
    this.empleadoDataSubject.next(this.empleado);
  }

  onRolChange(newRol: number) {
    this.empleado.rol = newRol;
    this.empleadoDataSubject.next(this.empleado);
  }

  onPasswordChange(newPassword: string) {
    this.empleado.password = newPassword;
    this.empleadoDataSubject.next(this.empleado);
    this.compararPassword();
  }

  onConfirmPasswordChange(newPassword: string) {
    this.empleado.confirm_password = newPassword;
    this.empleadoDataSubject.next(this.empleado);
    this.compararPassword();
  }

  onHorarioChange(newHorario: DayConfig[]) {
    this.empleado.horario = newHorario;
    this.empleadoDataSubject.next(this.empleado);
  }

  compararPassword(): boolean {
    if (this.empleado.password !== this.empleado.confirm_password) {
      this.toastr.error('Las contraseñas no coinciden');
      return false;
    }
    return true;
  }

  crearEmpleado() {
    if (this.compararPassword()) {
      console.log("Crear Empleado: ", this.empleado);
    }
  }

  modificarEmpleado() {
    if (this.activeButtonSave) {
      console.log("Actualizar Empleado: ", this.empleado);
    }
  }

  getRolesGenericos() {
    this.userService.getRolesGenericos().subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        const roles: Rol[] = data.map((rol: any) => {
          return {
            id: rol.id,
            nombre: rol.nombre
          }
        });
        this.roles = roles;
      },
      error: (error: ErrorApiResponse) => {
        console.log(error);
      }
    })
  }

  getDias() {
    this.diaService.getDias().subscribe({
      next: (response: ApiResponse) => {
        const dias = response.data ?? [];
        dias.forEach((dia: any) => {
          this.dataDias.push({
            id: dia.id,
            nombre: dia.nombre,
          } as Dia);
        });
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.message, 'Error al obtener los días');
      }
    });
  }
}
