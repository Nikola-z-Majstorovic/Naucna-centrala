import { TestBed } from '@angular/core/testing';

import { CoauthorService } from './coauthor.service';

describe('CoauthorService', () => {
  let service: CoauthorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CoauthorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
