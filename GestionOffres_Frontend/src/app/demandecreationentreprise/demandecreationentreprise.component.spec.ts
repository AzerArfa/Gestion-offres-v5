import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandecreationentrepriseComponent } from './demandecreationentreprise.component';

describe('DemandecreationentrepriseComponent', () => {
  let component: DemandecreationentrepriseComponent;
  let fixture: ComponentFixture<DemandecreationentrepriseComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemandecreationentrepriseComponent]
    });
    fixture = TestBed.createComponent(DemandecreationentrepriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
