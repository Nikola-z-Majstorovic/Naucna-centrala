import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TextSubbmitingComponent } from './text-subbmiting.component';

describe('TextSubbmitingComponent', () => {
  let component: TextSubbmitingComponent;
  let fixture: ComponentFixture<TextSubbmitingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TextSubbmitingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TextSubbmitingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
