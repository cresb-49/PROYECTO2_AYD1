import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  negocio = 'Negocios';
  servicios = 'Servicios';



  searchTypeMenuOpen = false;
  searchValue = '';
  selectedOption = this.servicios;

  constructor(private elementRef: ElementRef) { }

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

