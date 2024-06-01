import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  email: string = ''; // Initialize with an empty string
  errorMessage: string | null = null; // To store error message

  constructor(private router: Router) {}

  onSubmit() {
    
  }

}