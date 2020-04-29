import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChoosingErrorTimeComponent } from './choosing-error-time.component';

describe('ChoosingErrorTimeComponent', () => {
  let component: ChoosingErrorTimeComponent;
  let fixture: ComponentFixture<ChoosingErrorTimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChoosingErrorTimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChoosingErrorTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
