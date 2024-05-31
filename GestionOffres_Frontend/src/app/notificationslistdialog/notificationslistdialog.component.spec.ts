import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationslistdialogComponent } from './notificationslistdialog.component';

describe('NotificationslistdialogComponent', () => {
  let component: NotificationslistdialogComponent;
  let fixture: ComponentFixture<NotificationslistdialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationslistdialogComponent]
    });
    fixture = TestBed.createComponent(NotificationslistdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
