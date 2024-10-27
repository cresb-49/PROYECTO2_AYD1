import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServicioService } from '../../services/servicio/servicio.service';
import { ApiResponse, ErrorApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { CardAction, CardActionsComponent } from '../card-actions/card-actions.component';
import { FormsModule } from '@angular/forms';
@Component({
  standalone: true,
  imports: [CommonModule, CardActionsComponent, FormsModule],
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  negocio = 'Negocios';
  servicios = 'Servicios';

  searchTypeMenuOpen = false;
  searchValue = '';
  selectedOption = this.servicios;


  serviciosList: any[] = [];
  actions: CardAction[] = [
    { description: 'Ver', route: '/info-servicio', key: 'id', enabled: true },
  ]


  constructor(
    private elementRef: ElementRef,
    private servicioService: ServicioService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.servicioService.getServicios().subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        this.serviciosList = data;
      },
      error: (error: ErrorApiResponse) => {
        this.toastr.error(error.error, 'Error al obtener los servicios');
      }
    });
  }

  toogleSearchType() {
    this.searchTypeMenuOpen = !this.searchTypeMenuOpen;
    console.log('searchTypeMenuOpen', this.searchTypeMenuOpen);

  }

  setSelectOption(option: string) {
    this.selectedOption = option;
    this.searchTypeMenuOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    if (
      this.searchTypeMenuOpen &&
      !this.elementRef.nativeElement.querySelector('#dropdown-search-type').contains(event.target) &&
      !this.elementRef.nativeElement.querySelector('#dropdown-button-2').contains(event.target)
    ) {
      this.searchTypeMenuOpen = false;
    }
  }


  buscarServicios() {
    console.log('buscarServicios', this.searchValue);
    //Si el texto de busqueda esta vacio, se muestran todos los servicios
    // o si es una cadena vacia con espacios o tabs
    if (this.searchValue === '' || this.searchValue.trim() === '') {
      this.servicioService.getServicios().subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          this.serviciosList = data;
        },
        error: (error: ApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener los servicios');
        }
      });
    } else {
      this.servicioService.getServiciosLikeName(this.searchValue).subscribe({
        next: (response: ApiResponse) => {
          const data = response.data;
          this.serviciosList = data;
        },
        error: (error: ErrorApiResponse) => {
          this.toastr.error(error.error, 'Error al obtener los servicios');
        }
      });
    }
  }

}

