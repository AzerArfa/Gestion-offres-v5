import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddappeloffreComponent } from './addappeloffre.component';

describe('AddappeloffreComponent', () => {
  let component: AddappeloffreComponent;
  let fixture: ComponentFixture<AddappeloffreComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddappeloffreComponent]
    });
    fixture = TestBed.createComponent(AddappeloffreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
