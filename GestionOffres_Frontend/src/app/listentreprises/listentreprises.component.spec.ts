import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListentreprisesComponent } from './listentreprises.component';

describe('ListentreprisesComponent', () => {
  let component: ListentreprisesComponent;
  let fixture: ComponentFixture<ListentreprisesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListentreprisesComponent]
    });
    fixture = TestBed.createComponent(ListentreprisesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
