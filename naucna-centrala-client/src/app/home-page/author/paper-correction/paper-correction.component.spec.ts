import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaperCorrectionComponent } from './paper-correction.component';

describe('PaperCorrectionComponent', () => {
  let component: PaperCorrectionComponent;
  let fixture: ComponentFixture<PaperCorrectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaperCorrectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaperCorrectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
