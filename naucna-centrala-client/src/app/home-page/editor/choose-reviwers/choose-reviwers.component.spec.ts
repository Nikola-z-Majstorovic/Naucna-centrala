import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseReviwersComponent } from './choose-reviwers.component';

describe('ChooseReviwersComponent', () => {
  let component: ChooseReviwersComponent;
  let fixture: ComponentFixture<ChooseReviwersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChooseReviwersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseReviwersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
