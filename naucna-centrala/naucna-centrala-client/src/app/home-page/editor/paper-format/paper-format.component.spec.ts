import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaperFormatComponent } from './paper-format.component';

describe('PaperFormatComponent', () => {
  let component: PaperFormatComponent;
  let fixture: ComponentFixture<PaperFormatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaperFormatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaperFormatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
