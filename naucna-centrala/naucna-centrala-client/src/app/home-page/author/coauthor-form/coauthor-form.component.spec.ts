import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoauthorFormComponent } from './coauthor-form.component';

describe('CoauthorFormComponent', () => {
  let component: CoauthorFormComponent;
  let fixture: ComponentFixture<CoauthorFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoauthorFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoauthorFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
