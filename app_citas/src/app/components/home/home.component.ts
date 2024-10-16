import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServicioService } from '../../services/servicio/servicio.service';
import { ApiResponse } from '../../services/http/http.service';
import { ToastrService } from 'ngx-toastr';
import { CardAction, CardActionsComponent } from '../card-actions/card-actions.component';

@Component({
  standalone: true,
  imports: [CommonModule, CardActionsComponent],
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
      error: (error: ApiResponse) => {
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

}

