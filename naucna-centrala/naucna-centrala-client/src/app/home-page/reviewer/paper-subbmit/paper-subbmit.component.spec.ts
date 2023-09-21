import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaperSubbmitComponent } from './paper-subbmit.component';

describe('PaperSubbmitComponent', () => {
  let component: PaperSubbmitComponent;
  let fixture: ComponentFixture<PaperSubbmitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaperSubbmitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaperSubbmitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
