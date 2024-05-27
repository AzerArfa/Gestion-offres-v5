import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsappeloffreComponent } from './detailsappeloffre.component';

describe('DetailsappeloffreComponent', () => {
  let component: DetailsappeloffreComponent;
  let fixture: ComponentFixture<DetailsappeloffreComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailsappeloffreComponent]
    });
    fixture = TestBed.createComponent(DetailsappeloffreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
