import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemanderejointentrepriseComponent } from './demanderejointentreprise.component';

describe('DemanderejointentrepriseComponent', () => {
  let component: DemanderejointentrepriseComponent;
  let fixture: ComponentFixture<DemanderejointentrepriseComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemanderejointentrepriseComponent]
    });
    fixture = TestBed.createComponent(DemanderejointentrepriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
