import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionentrepriseComponent } from './actionentreprise.component';

describe('ActionentrepriseComponent', () => {
  let component: ActionentrepriseComponent;
  let fixture: ComponentFixture<ActionentrepriseComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActionentrepriseComponent]
    });
    fixture = TestBed.createComponent(ActionentrepriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
