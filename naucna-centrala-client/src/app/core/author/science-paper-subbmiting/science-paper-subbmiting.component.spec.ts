import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SciencePaperSubbmitingComponent } from './science-paper-subbmiting.component';

describe('SciencePaperSubbmitingComponent', () => {
  let component: SciencePaperSubbmitingComponent;
  let fixture: ComponentFixture<SciencePaperSubbmitingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SciencePaperSubbmitingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SciencePaperSubbmitingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
