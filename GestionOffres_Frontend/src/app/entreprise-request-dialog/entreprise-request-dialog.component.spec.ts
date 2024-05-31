import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntrepriseRequestDialogComponent } from './entreprise-request-dialog.component';

describe('EntrepriseRequestDialogComponent', () => {
  let component: EntrepriseRequestDialogComponent;
  let fixture: ComponentFixture<EntrepriseRequestDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EntrepriseRequestDialogComponent]
    });
    fixture = TestBed.createComponent(EntrepriseRequestDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
