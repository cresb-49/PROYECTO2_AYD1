import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ScheduleConfComponent } from '../../components/schedule-conf/schedule-conf.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ScheduleConfComponent],
  selector: 'app-crear-servicio',
  templateUrl: './crear-servicio.component.html',
  styleUrls: ['./crear-servicio.component.css']
})
export class CrearServicioComponent implements OnInit {

  activeButtonSave = false;
  imageSrc: string = 'no-image-found.png'; // URL por defecto de la imagen

  constructor() { }

  ngOnInit() {}

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageSrc = e.target.result; // Actualiza la URL de la imagen
      };
      reader.readAsDataURL(file);
    }
  }

}
