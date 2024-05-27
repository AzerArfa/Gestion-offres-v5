import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomevisitorComponent } from './homevisitor.component';

describe('HomevisitorComponent', () => {
  let component: HomevisitorComponent;
  let fixture: ComponentFixture<HomevisitorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HomevisitorComponent]
    });
    fixture = TestBed.createComponent(HomevisitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
