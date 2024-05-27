import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppeloffresadminComponent } from './appeloffresadmin.component';

describe('AppeloffresadminComponent', () => {
  let component: AppeloffresadminComponent;
  let fixture: ComponentFixture<AppeloffresadminComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppeloffresadminComponent]
    });
    fixture = TestBed.createComponent(AppeloffresadminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
