/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CanchaService } from './cancha.service';

describe('Service: Cancha', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CanchaService]
    });
  });

  it('should ...', inject([CanchaService], (service: CanchaService) => {
    expect(service).toBeTruthy();
  }));
});
