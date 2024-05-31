import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { ToastrService } from 'ngx-toastr'; // Import ToastrService

@Component({
  selector: 'app-password-update-dialog',
  templateUrl: './password-update-dialog.component.html',
  styleUrls: ['./password-update-dialog.component.css']
})
export class PasswordUpdateDialogComponent {
  passwordForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<PasswordUpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private userService: UserService,
    private toastr: ToastrService // Inject ToastrService
  ) {
    this.passwordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      newPasswordMatch: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(formGroup: FormGroup) {
    const newPassword = formGroup.get('newPassword')?.value;
    const newPasswordMatch = formGroup.get('newPasswordMatch')?.value;
    return newPassword === newPasswordMatch ? null : { mismatch: true };
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.passwordForm.valid) {
      this.userService.updatePasswordByEmail(this.passwordForm.value).subscribe(
        response => {
          console.log('Password updated successfully', response);
          this.toastr.success(response.message);
          this.dialogRef.close();
        },
        error => {
          console.error('Error updating password', error);
          this.toastr.error(error.error.message || 'Error updating password');
        }
      );
    }
  }
}
