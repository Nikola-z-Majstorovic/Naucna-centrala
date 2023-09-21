import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorTasksComponent } from './author-tasks.component';

describe('AuthorTasksComponent', () => {
  let component: AuthorTasksComponent;
  let fixture: ComponentFixture<AuthorTasksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorTasksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
