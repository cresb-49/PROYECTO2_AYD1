/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { DiaService } from './dia.service';

describe('Service: Dia', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DiaService]
    });
  });

  it('should ...', inject([DiaService], (service: DiaService) => {
    expect(service).toBeTruthy();
  }));
});
