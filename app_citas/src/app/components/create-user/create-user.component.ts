import { Component, Input, OnInit, resolveForwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DayConfig, ScheduleConfComponent } from '../schedule-conf/schedule-conf.component';
import { EmpleadoUpdateCreate, HorarioEmpleado, Rol, UpdateUserPassword, UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, debounceTime, filter } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Dia, DiaService } from '../../services/dia/dia.service';
import { ActivatedRoute } from '@angular/router';

export interface CreateUpdateEmpleado {
  id_empleado?: number;
  id_usuario?: number;
  nombres: string;
  apellidos: string;
  email: string;
  phone: string;
  password: string;
  current_password?: string;
  confirm_password?: string;
  cui: string;
  nit?: string;
  rol: number;
  horario: DayConfig[];
}

export interface UpdatePassword {
  password: string;
  current_password: string;
  confirm_password: string;
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
  activeChangePassword = false;

  roles: Rol[] = [];
  dataDias: Dia[] = []

  changePassword: UpdatePassword = {
    password: '',
    confirm_password: '',
    current_password: ''
  }

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
  private changePassworSubject = new BehaviorSubject<UpdatePassword>(this.changePassword)

  constructor(
    private toastr: ToastrService,
    private userService: UserService,
    private diaService: DiaService,
    private route: ActivatedRoute
  ) { }

  async ngOnInit() {
    await this.getRolesGenericos();
    await this.getDias();
    if (this.modificar) {
      await this.cargarDatosEmpleado();
    }
    this.empleadoDataSubject.pipe(
      debounceTime(300) // Evitar múltiples comparaciones inmediatas
    ).subscribe(newData => {
      if (this.modificar) {
        this.activeButtonSave = !this.compararObjetos(newData, this.empleadoOrignal);
        if (this.activeButtonSave) {
          this.toastr.info('Hay cambios pendientes por guardar');
        }
      } else {
        this.activeButtonSave = true;
      }
    });
    if (!this.modificar) {
      const horario = await this.calcularHorario([]);
      this.empleado.horario = horario;
    }
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
          init: '08:00',
          end: '18:00',
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
    if ((this.empleado.password ?? '').length <= 0) {
      this.toastr.error('Debe de asignar una contraseña')
      return false;
    }
    if (this.empleado.password !== this.empleado.confirm_password) {
      this.toastr.error('Las contraseñas no coinciden');
      return false;
    }
    return true;
  }

  accionBoton() {
    if (this.modificar) {
      this.modificarEmpleado()
    } else {
      this.crearEmpleado()
    }
  }


  crearEmpleado() {
    if (this.compararPassword()) {
      let rolSeleccionado = null;
      for (const rol of this.roles) {
        if (Number(this.empleado.rol) == Number(rol.id)) {
          rolSeleccionado = rol;
          break;
        }
      }
      if (rolSeleccionado != null && rolSeleccionado.id != 0) {
        const payloadEmpleado: EmpleadoUpdateCreate = {
          usuario: {
            id: this.empleado.id_usuario,
            nombres: this.empleado.nombres,
            apellidos: this.empleado.apellidos,
            cui: this.empleado.cui,
            email: this.empleado.email,
            nit: this.empleado.nit,
            phone: this.empleado.phone,
            password: this.empleado.password,
          },
          rol: rolSeleccionado,
          horarios: this.obtenerHorarioEmpleado(this.empleado.horario)
        }
        this.userService.crearEmpleado(payloadEmpleado).subscribe({
          next: async (response: ApiResponse) => {
            this.toastr.success('Empleado creado con exito', 'Empleado Creado');
            this.empleado = {
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
            const horario = await this.calcularHorario([]);
            this.empleado.horario = horario;
          },
          error: (error: ErrorApiResponse) => {
            this.toastr.error(error.error, 'Error al crear el empleado');
          }
        });
      } else {
        this.toastr.error('Debe de seleccionar un rol para el empleado', 'Error al crear el empleado');
      }
    }
  }

  obtenerHorarioEmpleado(horario: DayConfig[]): HorarioEmpleado[] {
    //Filtramos solo los dias activos
    const diasActivos = horario.filter(confDay => confDay.active)
    //por cada dia antivo buscamos el dia asociado
    let horarioFinal: HorarioEmpleado[] = []
    for (const dia of diasActivos) {
      const diaEcontrado = this.dataDias.find(diaData => diaData.nombre == dia.day)
      if (diaEcontrado) {
        horarioFinal.push(
          {
            dia: diaEcontrado,
            entrada: dia.init,
            salida: dia.end
          }
        )
      }
    }
    return horarioFinal;
  }

  modificarEmpleado() {
    if (this.activeButtonSave) {
      let rolSeleccionado = null;
      for (const rol of this.roles) {
        if (Number(this.empleado.rol) == Number(rol.id)) {
          rolSeleccionado = rol;
          break;
        }
      }
      if (rolSeleccionado != null && rolSeleccionado.id != 0) {
        const payloadEmpleado: EmpleadoUpdateCreate = {
          usuario: {
            id: this.empleado.id_usuario,
            nombres: this.empleado.nombres,
            apellidos: this.empleado.apellidos,
            cui: this.empleado.cui,
            email: this.empleado.email,
            nit: this.empleado.nit,
            phone: this.empleado.phone
          },
          rol: rolSeleccionado,
          horarios: this.obtenerHorarioEmpleado(this.empleado.horario)
        }
        console.log("Actualizar Empleado: ", payloadEmpleado);
      } else {
        this.toastr.error('Debe de seleccionar un rol para el empleado', 'Error al crear el empleado');
      }
    }
  }

  getRolesGenericos(): Promise<void> {
    return new Promise((resolve, reject) => {
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
          resolve(); // Resolvemos la promesa cuando se obtienen los roles
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.message, 'Error al obtener los roles');
          reject(error);
        }
      })
    });
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

  cargarDatosEmpleado() {
    const id_empleado = this.route.snapshot.paramMap.get('id');
    this.userService.getEmpleado(Number(id_empleado)).subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        this.empleado.id_empleado = data.id;
        this.empleado.id_usuario = data.usuario.id;
        this.empleado.nombres = data.usuario.nombres;
        this.empleado.apellidos = data.usuario.apellidos;
        this.empleado.email = data.usuario.email;
        this.empleado.phone = data.usuario.phone;
        this.empleado.nit = data.usuario.nit;
        this.empleado.cui = data.usuario.cui;
        const rolesObtenidos = data.usuario.roles.map((rol: any) => rol.rol.id);
        this.empleado.rol = this.filtrarRolesPermitidos(rolesObtenidos);
        this.empleado.horario = [...this.calcularHorario(data.horarios)];
        //Ahora una copia de los datos originales
        this.empleadoOrignal.id_empleado = data.id;
        this.empleadoOrignal.id_usuario = data.usuario.id;
        this.empleadoOrignal.nombres = data.usuario.nombres;
        this.empleadoOrignal.apellidos = data.usuario.apellidos;
        this.empleadoOrignal.email = data.usuario.email;
        this.empleadoOrignal.phone = data.usuario.phone;
        this.empleadoOrignal.nit = data.usuario.nit;
        this.empleadoOrignal.cui = data.usuario.cui;
        this.empleadoOrignal.rol = this.filtrarRolesPermitidos([...rolesObtenidos]);
        this.empleadoOrignal.horario = [...this.calcularHorario(data.horarios)];
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener el empleado');
      }
    });
  }

  private filtrarRolesPermitidos(roles: any[]): any {
    let rolesPermitidos: any[] = [];
    let rolesApp = this.roles.map((rol: Rol) => rol.id);
    roles.forEach((rol: any) => {
      if (rolesApp.includes(rol)) {
        rolesPermitidos.push(rol);
      }
    });
    return rolesPermitidos[0] ?? 0;
  }

  changePasswordAction() {
    if (this.activeChangePassword) {
      const payload: UpdateUserPassword = {
        id: Number(this.empleado.id_usuario),
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
}
