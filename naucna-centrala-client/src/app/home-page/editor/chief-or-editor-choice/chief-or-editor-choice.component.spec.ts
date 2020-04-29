import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChiefOrEditorChoiceComponent } from './chief-or-editor-choice.component';

describe('ChiefOrEditorChoiceComponent', () => {
  let component: ChiefOrEditorChoiceComponent;
  let fixture: ComponentFixture<ChiefOrEditorChoiceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChiefOrEditorChoiceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChiefOrEditorChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
