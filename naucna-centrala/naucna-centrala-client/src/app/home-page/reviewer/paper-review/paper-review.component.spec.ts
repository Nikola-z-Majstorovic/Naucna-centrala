import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaperReviewComponent } from './paper-review.component';

describe('PaperReviewComponent', () => {
  let component: PaperReviewComponent;
  let fixture: ComponentFixture<PaperReviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaperReviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaperReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
