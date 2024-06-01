import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  newPassword!: string;
  confirmPassword!: string;
  passwordMismatch: boolean = false;

  onSubmit() {
    if (this.newPassword !== this.confirmPassword) {
      this.passwordMismatch = true;
      return;
    }

    // Handle the password change logic here
    console.log('Password changed successfully');
  }

  onClose() {
    // Handle the close button action here
    console.log('Change password dialog closed');
  }
}
