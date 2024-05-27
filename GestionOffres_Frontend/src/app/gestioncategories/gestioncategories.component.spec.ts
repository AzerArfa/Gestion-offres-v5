import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestioncategoriesComponent } from './gestioncategories.component';

describe('GestioncategoriesComponent', () => {
  let component: GestioncategoriesComponent;
  let fixture: ComponentFixture<GestioncategoriesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GestioncategoriesComponent]
    });
    fixture = TestBed.createComponent(GestioncategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
