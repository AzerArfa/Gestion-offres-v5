import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordChangeWarningComponent } from './password-change-warning.component';

describe('PasswordChangeWarningComponent', () => {
  let component: PasswordChangeWarningComponent;
  let fixture: ComponentFixture<PasswordChangeWarningComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PasswordChangeWarningComponent]
    });
    fixture = TestBed.createComponent(PasswordChangeWarningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
