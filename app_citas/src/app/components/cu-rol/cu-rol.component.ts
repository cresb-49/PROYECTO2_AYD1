import { Component, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Permiso, Rol, RolService, UpdateRole } from '../../services/rol/rol.service';
import { ActivatedRoute } from '@angular/router';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';

export interface RolePermission {
  id: number;
  name: string;
  assigned: boolean;
}

export interface Role {
  id?: number;
  name: string;
  permissions: RolePermission[];
}

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-cu-rol',
  templateUrl: './cu-rol.component.html',
  styleUrls: ['./cu-rol.component.css']
})
export class CuRolComponent implements OnInit {
  @Input() modificar = false;
  activeButtonSave = false;

  permisos: Map<number, RolePermission> = new Map();

  role: Role = {
    name: '',
    permissions: [],
  };

  roleOriginal: Role = {
    name: '',
    permissions: [],
  };

  private roleDataSubject = new BehaviorSubject<Role>(this.role); // Para observar cambios

  constructor(
    private toastr: ToastrService,
    private rolService: RolService,
    private route: ActivatedRoute

  ) { }

  async ngOnInit() {
    await this.getPermisos();
    this.roleDataSubject.pipe().subscribe((newData) => {
      this.activeButtonSave = !this.compararObjetos(newData, this.roleOriginal);
      if (this.modificar && this.activeButtonSave) {
        this.toastr.info('Hay cambios pendientes por guardar');
      }
    });
    if (this.modificar) {
      this.cargarDatosRole();
    } else {
      this.role.permissions = this.calcularPermisos([]);
      this.activeButtonSave = true;
    }
  }

  getPermisos(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.rolService.getPermisos().subscribe({
        next: (responponse: ApiResponse) => {
          const data = responponse.data;
          data.forEach((permiso: any) => {
            this.permisos.set(permiso.id, {
              id: permiso.id,
              name: permiso.nombre,
              assigned: false,
            });
          });
          resolve(responponse.data);
        },
        error: (error: ErrorApiResponse) => {
          reject(error.error);
        }
      });
    });

  }

  onNombreChange(newNombre: string) {
    this.role.name = newNombre;
    this.roleDataSubject.next(this.role);
  }

  onPermissionToggle(permissionName: string) {
    const index = this.role.permissions.findIndex((perm) => perm.name === permissionName);
    if (index !== -1) {
      this.role.permissions[index].assigned = !this.role.permissions[index].assigned;
    }
    this.roleDataSubject.next(this.role);
  }

  compararObjetos(obj1: Role, obj2: Role): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  accionBoton() {
    if (this.modificar) {
      this.modificarRole();
    } else {
      this.crearRole();
    }
  }

  crearRole() {
    this.toastr.success('Rol creado exitosamente');
    console.log('Rol a guardar:', this.role);

    // Lógica para guardar el rol
  }

  modificarRole() {
    if (this.modificar && this.activeButtonSave) {
      // Creamos el payload de la actualizacion del rol
      const payload1: UpdateRole = {
        idRol: Number(this.role.id),
        permisos: this.castPermisos(this.role.permissions),
      }
      const payload2: Rol = {
        id: Number(this.role.id),
        nombre: this.role.name
      }
      // Llamamos al servicio para actualizar el rol y luego los permisos
      this.rolService.updateRol(payload2).subscribe({
        next: (response: ApiResponse) => {
          this.toastr.success('Rol actualizado exitosamente');
          this.roleOriginal.name = this.role.name;
          this.roleDataSubject.next(this.role);
          this.rolService.updatePermisosRole(payload1).subscribe({
            next: (response: ApiResponse) => {
              this.toastr.success('Permisos actualizados exitosamente');
              this.roleOriginal = JSON.parse(JSON.stringify(this.role));
              this.roleDataSubject.next(this.role);
            },
            error: (error: ErrorApiResponse) => {
              this.toastr.error(error.error, 'Error al actualizar los permisos');
            }
          });
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al actualizar el rol');
        }
      });
    }
  }

  castPermisos(permisos: RolePermission[]): Permiso[] {
    let permisosCast: Permiso[] = [];
    //Recorremos los permisos y solo agregamos los que esten asignados
    permisos.forEach((permiso) => {
      if (permiso.assigned) {
        permisosCast.push({
          id: permiso.id,
          nombre: permiso.name,
        });
      }
    });
    return permisosCast;
  }

  cargarDatosRole() {
    //Obtenemos id del rol de la URL
    const id = this.route.snapshot.paramMap.get('id');
    console.log('ID del rol a modificar:', id);
    this.rolService.getRolById(Number(id)).subscribe({
      next: (responponse: ApiResponse) => {
        const data = responponse.data;
        console.log('Datos del rol:', data);
        this.role.id = data.id;
        this.role.name = data.nombre;
        this.role.permissions = this.calcularPermisos(this.extraerPermisos(data.permisosRol));
        //Asignamos los permisos al rol original
        this.roleOriginal = JSON.parse(JSON.stringify(this.role));
        this.roleDataSubject.next(this.role);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al cargar los datos del rol');
      }
    });
  }

  extraerPermisos(permisosRol: any[]): RolePermission[] {
    return permisosRol.map((permisoRol) => {
      return {
        id: permisoRol.permiso.id,
        name: permisoRol.permiso.nombre,
        assigned: true,
      };
    });
  }

  calcularPermisos(permisosAsignados: RolePermission[]): RolePermission[] {
    //Creamos una copia del mapa de permisos
    const permisos = new Map(this.permisos);
    //recorremos los permisos asignados
    permisosAsignados.forEach((permiso) => {
      permisos.set(permiso.id, permiso);
    });
    return Array.from(permisos.values());
  }
}
