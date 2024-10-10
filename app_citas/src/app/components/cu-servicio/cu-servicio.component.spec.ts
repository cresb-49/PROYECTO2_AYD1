/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CuServicioComponent } from './cu-servicio.component';

describe('CuServicioComponent', () => {
  let component: CuServicioComponent;
  let fixture: ComponentFixture<CuServicioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CuServicioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CuServicioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
