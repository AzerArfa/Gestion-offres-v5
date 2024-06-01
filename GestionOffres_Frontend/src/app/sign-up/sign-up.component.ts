import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit{
  isSubmitting:boolean=false;
  newUser: User = new User();
  selectedFile: File | null = null;  // Allow null as a valid value
  confirmPassword: string = '';
  captcha: boolean = false;
  imagePreview: string | ArrayBuffer | null = null;

  constructor(private userService: UserService, private router: Router, private toastr: ToastrService) { }
ngOnInit(): void {
  this.resetForm();
}
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  triggerFileInput(): void {
    document.getElementById('fileInput')!.click();
  }

  resolved(captchaResponse: string | null): void {
    this.captcha = captchaResponse !== null;
  }

  onSubmit(): void {
    if (this.isSubmitting) {
      return; // Prevent duplicate form submission
    }

    if (this.confirmPassword !== this.newUser.password) {
      this.toastr.error('Les mots de passe ne correspondent pas!', 'Sign up', {
        timeOut: 5000,
        closeButton: true,
        progressBar: true,
        positionClass: 'toast-top-right',
      });
      return; // Password confirmation check
    }

    // Additional form validation checks

    this.isSubmitting = true; // Set submitting flag to true

    const formData: FormData = new FormData();
    formData.append('cin', this.newUser.cin);
    formData.append('email', this.newUser.email);
    formData.append('name', this.newUser.name);
    formData.append('prenom', this.newUser.prenom);
    const formattedDate = this.formatDate(new Date(this.newUser.datenais));
    formData.append('datenais', formattedDate);
    formData.append('lieunais', this.newUser.lieunais);
    formData.append('password', this.newUser.password);
    if (this.selectedFile) {
      formData.append('img', this.selectedFile, this.selectedFile.name);
    }
  
    this.userService.signup(formData).subscribe(response => { 
      this.toastr.success("Inscription réussie. Veuillez vérifier votre email pour activer votre compte.", 'Signup', {
        timeOut: 5000,
        closeButton: true,
        progressBar: true,
        positionClass: 'toast-top-right',
      });      
      console.log('User signed up successfully', response);
      this.router.navigate(['/login']);
      this.resetForm(); // Reset form state
    }, error => {
      this.toastr.error('Enregistrement échoué', 'Sign up', {
        timeOut: 5000,
        closeButton: true,
        progressBar: true,
        positionClass: 'toast-top-right',
      });
      console.error('Error signing up user', error);
      this.isSubmitting = false; // Reset submitting flag
    });
  }
  resetForm(): void {
    this.newUser = new User();
    this.selectedFile = null;
    this.confirmPassword = '';
    this.captcha = false;
    this.imagePreview = null;
    this.isSubmitting = false;
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // Add 1 because months are zero-indexed
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }
}
