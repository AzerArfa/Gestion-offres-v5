import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Notification } from '../model/notification.model';

@Component({
  selector: 'app-notificationslistdialog',
  templateUrl: './notificationslistdialog.component.html',
  styleUrls: ['./notificationslistdialog.component.css']
})
export class NotificationslistdialogComponent {
  constructor(
    public dialogRef: MatDialogRef<NotificationslistdialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {} closeDialog(): void {
    this.dialogRef.close();
  }
}
