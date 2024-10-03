import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DayConfig, ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';
import { FormsModule } from '@angular/forms';
import { Dia, Horario, Negocio, NegocioService, PayloadNegocio } from '../../services/negocio/negocio.service';
import { ToastrService } from 'ngx-toastr';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { BehaviorSubject, debounceTime } from 'rxjs';
import { DiaService } from '../../services/dia/dia.service';

export interface ManageNegocio {
  id: number;
  nombre: string;
  logo: string;
  asignacion_manual: boolean;
  direccion: string;
  horarios: DayConfig[];
}

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

  dataDias: Dia[] = []

  negocioOriginalData: ManageNegocio = {
    id: 0,
    nombre: '',
    logo: '',
    asignacion_manual: false,
    direccion: '',
    horarios:[]
  };

  negocioData: ManageNegocio = {
    id: 0,
    nombre: '',
    logo: '',
    asignacion_manual: false,
    direccion: '',
    horarios: []
  };

  private negocioDataSubject = new BehaviorSubject<ManageNegocio>(this.negocioData); // Para observar cambios

  constructor(
    private toastr: ToastrService,
    private negocioService: NegocioService,
    private diaService: DiaService
  ) { }

  async ngOnInit() {
    await this.getDias();
    await this.cargarDatosNegocio();
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
            horarios: this.calcularHorario(response.data.horarios),
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

  calcularHorario(horarios:any):DayConfig[]{
    let negocioHas:DayConfig[] = horarios.map((horario: any) => {
      return {
        id: horario.dia.id,
        day: horario.dia.nombre,
        init: horario.apertura,
        end: horario.cierre,
        active: true
      } as DayConfig
    })
    //En el listado de dias buscamos por medio de id si ya esta agregado si no esta agregado
    //se agrega con active en false y init y end en 00:00
    this.dataDias.forEach((dia)=>{
      let index = negocioHas.findIndex((diaHas)=>{
        return diaHas.id === dia.id
      })
      if(index === -1){
        negocioHas.push({
          id: dia.id,
          day: dia.nombre,
          init: '00:00',
          end: '00:00',
          active: false
        } as DayConfig)
      }
    })
    return negocioHas;
  }


  // Comparación profunda entre dos objetos
  compararObjetos(obj1: ManageNegocio, obj2: ManageNegocio): boolean {
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

  getDias() {
    this.diaService.getDias().subscribe({
      next: (response: ApiResponse) => {
        const dias = response.data ?? [];
        dias.forEach((dia: any) => {
          this.dataDias.push({
            id: dia.id,
            nombre: dia.nombre,
          }as Dia);
        });
        console.log(this.dataDias);
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.message, 'Error al obtener los días');
      }
    });
  }
}
