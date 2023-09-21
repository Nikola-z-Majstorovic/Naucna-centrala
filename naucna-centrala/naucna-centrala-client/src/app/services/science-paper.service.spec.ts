import { TestBed } from '@angular/core/testing';

import { SciencePaperService } from './science-paper.service';

describe('SciencePaperService', () => {
  let service: SciencePaperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SciencePaperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
