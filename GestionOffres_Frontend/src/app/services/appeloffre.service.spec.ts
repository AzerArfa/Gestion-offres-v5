import { TestBed } from '@angular/core/testing';

import { AppeloffreService } from './appeloffre.service';

describe('AppeloffreService', () => {
  let service: AppeloffreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppeloffreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
