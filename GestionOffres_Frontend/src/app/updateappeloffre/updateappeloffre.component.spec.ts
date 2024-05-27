import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateappeloffreComponent } from './updateappeloffre.component';

describe('UpdateappeloffreComponent', () => {
  let component: UpdateappeloffreComponent;
  let fixture: ComponentFixture<UpdateappeloffreComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateappeloffreComponent]
    });
    fixture = TestBed.createComponent(UpdateappeloffreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
