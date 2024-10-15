import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, debounceTime } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Servicio, ServicioService } from '../../services/servicio/servicio.service';
import { Rol, UserService } from '../../services/user/user.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ActivatedRoute } from '@angular/router';

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
    private servicioService: ServicioService,
    private route: ActivatedRoute,
  ) { }

  //Observadores de los cambios en los datos del servicio
  onNombreChange(event: any) {
    this.servicioDataSubject.next(this.servicioData);
  }

  onDetallesChange(event: any) {
    this.servicioDataSubject.next(this.servicioData);
  }

  onPrecioChange(event: any) {
    this.servicioDataSubject.next(this.servicioData);
  }

  onDuracionChange(event: any) {
    this.servicioDataSubject.next(this.servicioData);
  }

  onRolChange(event: any) {
    this.servicioDataSubject.next(this.servicioData);
  }

  async ngOnInit() {
    await this.getRolesGenericos();
    if (this.modificar) {
      await this.cargarDatosServicio();
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
    //Generamos el payload de carga de los datos
    //Obtenemos el rol de los roles seleccionados
    let rolSeleccionado = null;
    for (const rol of this.roles) {
      if (rol.id === this.servicioData.id_rol) {
        rolSeleccionado = rol;
        break
      }
    }
    //Verificamos que seleccionamos un rol
    if (rolSeleccionado != null) {
      const updateService: Servicio = {
        id: this.servicioData.id,
        costo: this.servicioData.precio,
        detalles: this.servicioData.detalles,
        duracion: this.servicioData.duracion,
        imagen: this.servicioData.imagen,
        nombre: this.servicioData.nombre,
        rol: rolSeleccionado
      };

      this.servicioService.updateServicio(updateService).subscribe({
        next: (response: ApiResponse) => {
          this.toastr.success('Servicio actualizado con exito', "Exito!!!");
          this.servicioData = {
            id: updateService.id,
            precio: updateService.costo,
            detalles: updateService.detalles,
            duracion: updateService.duracion,
            id_rol: updateService.rol.id,
            imagen: updateService.imagen,
            nombre: updateService.nombre
          }
          this.servicioOriginalData = {
            id: updateService.id,
            precio: updateService.costo,
            detalles: updateService.detalles,
            duracion: updateService.duracion,
            id_rol: updateService.rol.id,
            imagen: updateService.imagen,
            nombre: updateService.nombre
          };
          this.servicioDataSubject.next(this.servicioData);
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al actualizar el Servicio');
        }
      });
    }
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

  cargarDatosServicio(): Promise<void> {
    return new Promise((resolve, reject) => {
      //Obtenemos el paramento id de la url
      const id = this.route.snapshot.paramMap.get('id');
      this.servicioService.getServicio(id).subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          this.servicioData = {
            id: data.id,
            detalles: data.detalles,
            duracion: data.duracion,
            id_rol: data.rol.id,
            imagen: data.imagen,
            nombre: data.nombre,
            precio: data.costo
          }
          this.imageSrc = data.imagen;
          this.servicioOriginalData = {
            id: data.id,
            detalles: data.detalles,
            duracion: data.duracion,
            id_rol: data.rol.id,
            imagen: data.imagen,
            nombre: data.nombre,
            precio: data.costo
          }
          this.servicioDataSubject.next(this.servicioData);
          resolve();
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener el servicio')
          reject(error);
        }
      })
    });
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
