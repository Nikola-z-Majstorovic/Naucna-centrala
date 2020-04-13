import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseMagazineComponent } from './choose-magazine.component';

describe('ChooseMagazineComponent', () => {
  let component: ChooseMagazineComponent;
  let fixture: ComponentFixture<ChooseMagazineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChooseMagazineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseMagazineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
