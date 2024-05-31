import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppeloffreService } from '../services/appeloffre.service';
import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { Categorie } from '../model/categorie.model';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-addappeloffre',
  templateUrl: './addappeloffre.component.html',
  styleUrls: ['./addappeloffre.component.css']
})
export class AddappeloffreComponent implements OnInit {
  newAppelOffre: any = {
    titre: '',
    description: '',
    localisation: '',
    datelimitesoumission: '',
    categorieId: null
  };
  selectedFile: File | null = null;
  selectedDocument: File | null = null;
  entrepriseId!: string;
  datelimitesoumissionFormatted: string | null = null;
  categories: Categorie[] = [];
  imagePreview: string | ArrayBuffer | null = null;

  constructor(
    private appeloffreService: AppeloffreService,
    private userService:UserService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.entrepriseId = params['id'];
    });
    this.fetchCategories();
  }

  fetchCategories(): void {
    this.appeloffreService.getAllCategories().subscribe({
      next: categories => this.categories = categories,
      error: error => console.error('Error fetching categories', error)
    });
  }

  onImageSelected(event: any): void {
    if (event.target.files && event.target.files[0]) {
      this.selectedFile = event.target.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      if (this.selectedFile) {
        reader.readAsDataURL(this.selectedFile);
      }
    }
  }
  

  onDocumentSelected(event: any): void {
    if (event.target.files && event.target.files[0]) {
      this.selectedDocument = event.target.files[0];
    }
  }

  triggerFileInput(): void {
    document.getElementById('fileInput')!.click();
  }

  createAppelOffre(): void {
    const formData = new FormData();
    formData.append('titre', this.newAppelOffre.titre);
    formData.append('description', this.newAppelOffre.description);
    formData.append('localisation', this.newAppelOffre.localisation);
    formData.append('entrepriseId', this.entrepriseId);
    formData.append('categorieId', this.newAppelOffre.categorieId);
    if (this.datelimitesoumissionFormatted) {
      formData.append('datelimitesoumission', this.datelimitesoumissionFormatted);
    }
    if (this.selectedFile) {
      formData.append('img', this.selectedFile, this.selectedFile.name);
    }
    if (this.selectedDocument) {
      formData.append('document', this.selectedDocument, this.selectedDocument.name);
    }

    this.appeloffreService.createAppelOffre(formData).subscribe({
      next: (response) => { 
        console.log('Offer created successfully', response);
        console.log("ID APPELOFFRE", response.id);
        console.log("ID CATEGORIE", this.newAppelOffre.categorieId); // Change from response.categorie.id to response.categorieId
        this.createNotification(response.id, this.newAppelOffre.categorieId); // Pass the created AppelOffre ID to the notification creation method
        this.location.back();
        this.toastr.success('Création terminé avec succès', "Appel d'offre", {
          timeOut: 5000,
          closeButton: true,
          progressBar: true,
          positionClass: 'toast-top-right',
        });
      },
      error: (error) => {
        this.toastr.error('Connexion échouée', "Appel d'offre", {
          timeOut: 5000,
          closeButton: true,
          progressBar: true,
          positionClass: 'toast-top-right',
        });
        console.error('Error creating offer', error);
        alert('Failed to create the offer. Check console for details.');
      }
    });
}

createNotification(appelOffreId: string, id_categorie: string): void {
    const notification = {
        entreprise: { id: this.entrepriseId },
        id_appeloffre: appelOffreId,
        id_categorie: id_categorie
    };

    this.userService.addNotification(notification).subscribe({
        next: (response) => {
            console.log('Notification created successfully', response);
        },
        error: (error) => {
            console.error('Error creating notification', error);
        }
    });
}



}
