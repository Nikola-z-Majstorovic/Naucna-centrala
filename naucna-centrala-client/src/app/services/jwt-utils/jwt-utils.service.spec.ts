import { TestBed } from '@angular/core/testing';

import { JwtUtilsService } from './jwt-utils.service';

describe('JwtUtilsService', () => {
  let service: JwtUtilsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JwtUtilsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
