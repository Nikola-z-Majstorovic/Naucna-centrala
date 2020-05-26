import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseOtherReviwersComponent } from './choose-other-reviwers.component';

describe('ChooseOtherReviwersComponent', () => {
  let component: ChooseOtherReviwersComponent;
  let fixture: ComponentFixture<ChooseOtherReviwersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChooseOtherReviwersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseOtherReviwersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
