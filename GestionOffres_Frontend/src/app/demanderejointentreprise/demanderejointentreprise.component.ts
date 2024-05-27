import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-demanderejointentreprise',
  templateUrl: './demanderejointentreprise.component.html',
  styleUrls: ['./demanderejointentreprise.component.css']
})
export class DemanderejointentrepriseComponent implements OnInit {
  joinrequest: any = {
    entrepriseMatricule: '',
  };
  selectedFile: File | null = null;
  userId!: string | null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private authservice: AuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.authservice.getUserId().subscribe(id => {
      this.userId = id;
      console.log('User ID:', this.userId);
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  onSubmit(): void {
    const formData: FormData = new FormData();
    formData.append('entrepriseMatricule', this.joinrequest.entrepriseMatricule);

    this.userService.requestToJoinEntreprise(this.userId!, formData).subscribe(
      response => {
        console.log('Request added successfully:', response);
        this.router.navigate([`/profile/${this.userId}`]);
    
        this.toastr.success('Votre demande a été envoyé avec succées', 'Demande rejoint entreprise', {
          timeOut: 5000,
          closeButton: true,
          progressBar: true,
          positionClass: 'toast-top-right',
        });
        
      },
      error => {
        this.toastr.error('Votre demande n’a pas pu être traitée', 'Échec de la demande', {
          timeOut: 5000,
          closeButton: true,
          progressBar: true,
          positionClass: 'toast-top-right',
      });
      
        console.error('Error adding Request:', error);
        if (error.error) {
          console.error('Backend returned error message:', error.error);
        }
        if (error.status === 200) {
          console.error('Received status 200, but there was an error in the response.');
        }
      }
    );
  }
}
