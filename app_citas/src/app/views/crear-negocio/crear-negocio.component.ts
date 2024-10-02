import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { FormsModule } from '@angular/forms';
import { Negocio, NegocioService, PayloadNegocio } from '../../services/negocio/negocio.service';
import { ToastrService } from 'ngx-toastr';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { BehaviorSubject, debounceTime } from 'rxjs';
import { DiaService } from '../../services/dia/dia.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ScheduleConfComponent, FormsModule],
  selector: 'app-crear-negocio',
  templateUrl: './crear-negocio.component.html',
  styleUrls: ['./crear-negocio.component.css']
})
export class CrearNegocioComponent implements OnInit {
  activeButtonSave = false;
  imageSrc: string = 'no-image-found.png'; // URL por defecto de la imagen

  negocioOriginalData: Negocio = {
    id: 0,
    nombre: '',
    logo: '',
    asignacion_manual: false,
    direccion: '',
    horarios: []
  };

  negocioData: Negocio = {
    id: 0,
    nombre: '',
    logo: '',
    asignacion_manual: false,
    direccion: '',
    horarios: []
  };

  private negocioDataSubject = new BehaviorSubject<Negocio>(this.negocioData); // Para observar cambios

  constructor(
    private toastr: ToastrService,
    private negocioService: NegocioService,
    private diaService: DiaService
  ) { }

  ngOnInit() {
    this.cargarDatosNegocio();
    this.getDias();
    // Suscribirnos a los cambios en negocioData y comparar con negocioOriginalData
    this.negocioDataSubject.pipe(
      debounceTime(300) // Evitar múltiples comparaciones inmediatas
    ).subscribe(newData => {
      this.activeButtonSave = !this.compararObjetos(newData, this.negocioOriginalData);
    });
  }

  private cargarDatosNegocio() {
    this.negocioService.getNegocio().subscribe(
      {
        next: (response: ApiResponse) => {
          this.negocioData = {
            id: response.data.id,
            nombre: response.data.nombre,
            logo: response.data.logo,
            asignacion_manual: response.data.asignacion_manual,
            direccion: response.data.direccion,
            horarios: response.data.horarios
          };
          console.log(this.negocioData);

          this.imageSrc = response.data.logo;
          this.negocioOriginalData = { ...this.negocioData }; // Guardamos la copia original

          this.negocioDataSubject.next(this.negocioData); // Emitir el valor inicial de negocioData
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.message, 'Error al cargar los datos del negocio');
        }
      }
    );
  }

  // Comparación profunda entre dos objetos
  compararObjetos(obj1: Negocio, obj2: Negocio): boolean {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  // Se actualiza el campo nombre y emitimos el cambio
  onNombreChange(newNombre: string): void {
    this.negocioData.nombre = newNombre;
    this.negocioDataSubject.next(this.negocioData);
  }

  // Se actualiza el campo direccion y emitimos el cambio
  onDireccionChange(newDireccion: string): void {
    this.negocioData.direccion = newDireccion;
    this.negocioDataSubject.next(this.negocioData);
  }

  // Se actualiza el checkbox y emitimos el cambio
  onAsignacionManualChange(): void {
    this.negocioData.asignacion_manual = !this.negocioData.asignacion_manual;
    this.negocioDataSubject.next(this.negocioData);
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageSrc = e.target.result;
        this.negocioData.logo = e.target.result; // Actualizamos el logo
        this.negocioDataSubject.next(this.negocioData); // Emitimos el cambio en negocioData
      };
      reader.readAsDataURL(file);
    }
  }

  actualizarNegocio(): void {
    const payload: PayloadNegocio = {
      id: this.negocioData.id,
      nombre: this.negocioData.nombre,
      logo: this.negocioData.logo,
      asignacionManual: this.negocioData.asignacion_manual,
      direccion: this.negocioData.direccion
    };

    this.negocioService.updateNegocio(payload).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success('Negocio actualizado correctamente');
        this.negocioOriginalData = { ...this.negocioData }; // Actualizamos la copia original
        this.activeButtonSave = false; // Desactivamos el botón de guardar
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.message, 'Error al actualizar el negocio');
      }
    });
  }

  getDias(){
    this.diaService.getDias().subscribe({
      next: (response: ApiResponse) => {
        console.log(response.data);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.message, 'Error al obtener los días');
      }
    });
  }
}
