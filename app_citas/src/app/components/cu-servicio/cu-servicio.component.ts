import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, debounceTime } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { ServicioService } from '../../services/servicio/servicio.service';
import { Rol, UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';

export interface ManageServicio {
  id?: number;
  imagen: string;
  nombre: string;
  detalles: string;
  precio: number;
  duracion: number;
  id_rol: number;
}

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-cu-servicio',
  templateUrl: './cu-servicio.component.html',
  styleUrls: ['./cu-servicio.component.css']
})
export class CuServicioComponent implements OnInit {
  @Input() modificar = false;
  activeButtonSave = false;
  roles: Rol[] = [];
  imageSrc: string = 'no-image-found.png'; // URL por defecto de la imagen

  servicioData: ManageServicio = {
    imagen: this.imageSrc,
    nombre: '',
    detalles: '',
    precio: 1,
    duracion: 0.25,
    id_rol: 0
  };
  servicioOriginalData: ManageServicio = {
    imagen: '',
    nombre: '',
    detalles: '',
    precio: 1,
    duracion: 0.25,
    id_rol: 0
  };

  private servicioDataSubject = new BehaviorSubject<ManageServicio>(this.servicioData); // Para observar cambios

  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private servicioService: ServicioService
  ) { }

  //Observadores de los cambios en los datos del servicio
  onNombreChange(event: any) {
    this.servicioData.nombre = event.target.value;
    this.servicioDataSubject.next(this.servicioData);
  }

  onDetallesChange(event: any) {
    this.servicioData.detalles = event.target.value;
    this.servicioDataSubject.next(this.servicioData);
  }

  onPrecioChange(event: any) {
    this.servicioData.precio = event.target.value;
    this.servicioDataSubject.next(this.servicioData);
  }

  onDuracionChange(event: any) {
    this.servicioData.duracion = event.target.value;
    this.servicioDataSubject.next(this.servicioData);
  }

  onRolChange(event: any) {
    this.servicioData.id_rol = event.target.value;
    this.servicioDataSubject.next(this.servicioData);
  }

  async ngOnInit() {
    await this.getRolesGenericos();
    if (this.modificar) {
      this.cargarDatosServicio();
    }
    this.servicioDataSubject.pipe(
      debounceTime(300)
    ).subscribe(newData => {
      if (this.modificar) {
        this.activeButtonSave = !this.compararObjetos(newData, this.servicioOriginalData);
        if (this.activeButtonSave) {
          this.toastr.info('Hay cambios pendientes por guardar');
        }
      } else {
        this.activeButtonSave = true;
      }
    })
  }

  accionServicio() {
    if (this.modificar) {
      this.actualizarServicio();
    } else {
      this.crearServicio();
    }
  }

  crearServicio() {
    // this.servicioService.crearServicio(this.servicioData).subscribe({
    //   next: (response: ApiResponse) => {
    //     this.toastr.success('Servicio creado');
    //   },
    //   error: (error: ErrorApiResponse) => {
    //     this.toastr.error(error.message, 'Error al crear servicio');
    //   }
    // });
  }

  actualizarServicio() {
    // this.servicioService.actualizarServicio(this.servicioData).subscribe({
    //   next: (response: ApiResponse) => {
    //     this.toastr.success('Servicio actualizado');
    //   },
    //   error: (error: ErrorApiResponse) => {
    //     this.toastr.error(error.message, 'Error al actualizar servicio');
    //   }
    // });
  }

  compararObjetos(obj1: ManageServicio, obj2: ManageServicio): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }


  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageSrc = e.target.result;
        this.servicioData.imagen = e.target.result; // Actualizamos el logo
        this.servicioDataSubject.next(this.servicioData); // Emitimos el cambio en negocioData
      };
      reader.readAsDataURL(file);
    }
  }

  cargarDatosServicio() {

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

}
