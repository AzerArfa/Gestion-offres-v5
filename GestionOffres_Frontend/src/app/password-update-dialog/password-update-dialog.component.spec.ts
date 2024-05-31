import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordUpdateDialogComponent } from './password-update-dialog.component';

describe('PasswordUpdateDialogComponent', () => {
  let component: PasswordUpdateDialogComponent;
  let fixture: ComponentFixture<PasswordUpdateDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PasswordUpdateDialogComponent]
    });
    fixture = TestBed.createComponent(PasswordUpdateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
