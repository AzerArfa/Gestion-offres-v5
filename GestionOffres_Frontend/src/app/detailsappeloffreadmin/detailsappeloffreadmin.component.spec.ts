import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsappeloffreadminComponent } from './detailsappeloffreadmin.component';

describe('DetailsappeloffreadminComponent', () => {
  let component: DetailsappeloffreadminComponent;
  let fixture: ComponentFixture<DetailsappeloffreadminComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailsappeloffreadminComponent]
    });
    fixture = TestBed.createComponent(DetailsappeloffreadminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
