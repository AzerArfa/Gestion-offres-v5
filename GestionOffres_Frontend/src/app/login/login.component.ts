import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import Swal from 'sweetalert2';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user = { email: '', password: '' };
  err = 0;
  message: string | null = null;

  constructor(
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private toastr:ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = this.activatedRoute.snapshot.queryParamMap.get('token');
    if (token) {
      this.authService.verifyEmail(token).subscribe(
        response => {
          this.message = 'E-mail vérifié avec succès!';
          Swal.fire({
            icon: 'success',
            title: 'Verification',
            text: this.message,
            showConfirmButton: true,
            timer: 5000
          });
        },
        error => {
          this.message = "La vérification de l'e-mail a échoué. Veuillez réessayer.";
          Swal.fire({
            icon: 'error',
            title: 'Verification',
            text: this.message,
            showConfirmButton: true,
            timer: 5000
          });
        }
      );
    }
  }

  Login(): void {
    console.log('Attempting to log in with user:', this.user); // Log the user attempting to log in
    this.authService.login(this.user).subscribe(
      response => {
        console.log('Login successful:', response); // Log the successful login response
        this.router.navigate(['/actionentreprise']); // Redirect to the users page after login
      },
      error => {
        console.log('Login error:', error); // Log any login error
        if (error.status === 403 && error.error === "Password update required") {
          this.toastr.warning("Veuillez mettre à jour votre mot de passe.", 'Login', {
            timeOut: 5000,
            closeButton: true,
            progressBar: true,
            positionClass: 'toast-top-right',
          });
        } else {
          this.toastr.error("Le mot de passe ou l'e-mail sont erronés", 'Login', {
            timeOut: 5000,
            closeButton: true,
            progressBar: true,
            positionClass: 'toast-top-right',
          });
        }
        this.err = 1;
      }
    );
  }
}
