import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LikeDislikeComponent } from './like-dislike.component';

describe('LikeDislikeComponent', () => {
  let component: LikeDislikeComponent;
  let fixture: ComponentFixture<LikeDislikeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LikeDislikeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LikeDislikeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
