import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsulterentrepriseComponent } from './consulterentreprise.component';

describe('ConsulterentrepriseComponent', () => {
  let component: ConsulterentrepriseComponent;
  let fixture: ComponentFixture<ConsulterentrepriseComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConsulterentrepriseComponent]
    });
    fixture = TestBed.createComponent(ConsulterentrepriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
