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
      if(this.modificar){
        this.activeButtonSave = !this.compararObjetos(newData, this.empleadoOrignal);
        if (this.activeButtonSave) {
          this.toastr.info('Hay cambios pendientes por guardar');
        }
      }else{
        this.activeButtonSave = true;
      }
    });
    if (!this.modificar) {
      const horario = await this.calcularHorario([]);
      this.empleado.horario = horario;
    }
  }

  // Comparación profunda entre dos objetos
  compararObjetos(obj1: CreateUpdateEmpleado, obj2: CreateUpdateEmpleado): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  calcularHorario(horarios: any): DayConfig[] {
    let horarioHas: DayConfig[] = horarios.map((horario: any) => {
      return {
        id: horario.dia.id,
        day: horario.dia.nombre,
        init: horario.entrada,
        end: horario.salida,
        active: true
      } as DayConfig
    })
    //En el listado de dias buscamos por medio de id si ya esta agregado si no esta agregado
    //se agrega con active en false y init y end en 00:00
    this.dataDias.forEach((dia) => {
      let index = horarioHas.findIndex((diaHas) => {
        return diaHas.id === dia.id
      })
      if (index === -1) {
        horarioHas.push({
          id: dia.id,
          day: dia.nombre,
          init: '00:00',
          end: '00:00',
          active: false
        } as DayConfig)
      }
    })
    return horarioHas;
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

  getDias(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.diaService.getDias().subscribe({
        next: (response: ApiResponse) => {
          const dias = response.data ?? [];
          dias.forEach((dia: any) => {
            this.dataDias.push({
              id: dia.id,
              nombre: dia.nombre,
            } as Dia);
          });
          resolve(); // Resolvemos la promesa cuando se obtienen los días
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.message, 'Error al obtener los días');
          reject(error);
        }
      });
    });
  }
}
