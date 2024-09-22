/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { SeeCourtComponent } from './see-court.component';

describe('SeeCourtComponent', () => {
  let component: SeeCourtComponent;
  let fixture: ComponentFixture<SeeCourtComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeeCourtComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeeCourtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
