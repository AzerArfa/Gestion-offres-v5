import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RechercheUsersComponent } from './recherche-users.component';

describe('RechercheUsersComponent', () => {
  let component: RechercheUsersComponent;
  let fixture: ComponentFixture<RechercheUsersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RechercheUsersComponent]
    });
    fixture = TestBed.createComponent(RechercheUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
