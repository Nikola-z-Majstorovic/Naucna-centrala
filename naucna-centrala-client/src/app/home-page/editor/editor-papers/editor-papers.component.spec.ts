import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorPapersComponent } from './editor-papers.component';

describe('EditorPapersComponent', () => {
  let component: EditorPapersComponent;
  let fixture: ComponentFixture<EditorPapersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorPapersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorPapersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
