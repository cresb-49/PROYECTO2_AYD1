/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CuRolComponent } from './cu-rol.component';

describe('CuRolComponent', () => {
  let component: CuRolComponent;
  let fixture: ComponentFixture<CuRolComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CuRolComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CuRolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
