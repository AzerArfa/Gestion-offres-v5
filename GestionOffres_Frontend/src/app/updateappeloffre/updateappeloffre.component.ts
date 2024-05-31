import { Component, Inject, OnInit } from '@angular/core';
import { AppeloffreService } from '../services/appeloffre.service';
import { AppelOffre } from '../model/appeloffre.model';
import { Categorie } from '../model/categorie.model';
import { formatDate } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-updateappeloffre',
  templateUrl: './updateappeloffre.component.html',
  styleUrls: ['./updateappeloffre.component.css']
})
export class UpdateappeloffreComponent implements OnInit {
  currentAppelOffre: AppelOffre = new AppelOffre();
  selectedFile: File | null = null;
  selectedDocument: File | null = null;
  datelimitesoumissionFormatted!: string;
  showFileInput: boolean = false;
  offreImagePreview: string | ArrayBuffer | null = null;
  categories: Categorie[] = []; // List of categories
  selectedCategorieId: string | null = null; // Selected category ID

  constructor(
    private appeloffreService: AppeloffreService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private location: Location,
    private toastr: ToastrService,
    public dialogRef: MatDialogRef<UpdateappeloffreComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    const id = this.data.id; // Get the id from injected data
    console.log("ID:", id);
    this.loadCategories(); // Load categories when the component initializes

    this.appeloffreService.getAppelOffreById(id).subscribe(
      currentAppelOffre => {
        this.currentAppelOffre = currentAppelOffre;
        this.selectedCategorieId = currentAppelOffre.categorie.id; // Set the default selected category
        if (currentAppelOffre.datelimitesoumission) {
          this.datelimitesoumissionFormatted = formatDate(currentAppelOffre.datelimitesoumission, 'yyyy-MM-dd', 'en-US');
        }

        if (this.currentAppelOffre.img) {
          this.offreImagePreview = `data:image/jpeg;base64,${this.currentAppelOffre.img}`;
          this.selectedFile = new File([this.base64ToBlob(this.currentAppelOffre.img)], 'currentImage.jpg', { type: 'image/jpeg' });
        }

        if (this.currentAppelOffre.document) {
          this.selectedDocument = new File([this.base64ToBlob(this.currentAppelOffre.document)], 'currentDocument.pdf', { type: 'application/pdf' });
        }

        console.log('Loaded currentAppelOffre:', this.currentAppelOffre);
      },
      error => {
        this.toastr.error('Modification échouée', "Appel d'offre", {
          timeOut: 5000,
          closeButton: true,
          progressBar: true,
          positionClass: 'toast-top-right',
        });
        console.error('Error loading currentAppelOffre:', error);
      }
    );
  }

  loadCategories(): void {
    this.appeloffreService.getAllCategories().subscribe(
      categories => {
        this.categories = categories;
      },
      error => {
        this.toastr.error('Failed to load categories', "Appel d'offre", {
          timeOut: 5000,
          closeButton: true,
          progressBar: true,
          positionClass: 'toast-top-right',
        });
        console.error('Error loading categories:', error);
      }
    );
  }

  base64ToBlob(base64: string): Blob {
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    return new Blob([byteArray]);
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.showFileInput = false;
      const reader = new FileReader();
      reader.onload = (e) => {
        this.offreImagePreview = e.target?.result as string | ArrayBuffer | null;
      };
      reader.readAsDataURL(file);
      console.log('Selected file:', this.selectedFile);
    } else {
      this.selectedFile = null;
      this.showFileInput = false;
      this.offreImagePreview = this.currentAppelOffre.img;
    }
  }

  onDocumentSelected(event: any): void {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.selectedDocument = file;
      console.log('Selected document:', this.selectedDocument);
    } else {
      this.selectedDocument = null;
    }
  }

  onCategorieChange(event: any): void {
    this.selectedCategorieId = event.target.value;
  }

  updateAppelOffre(): void {
    const formData = new FormData();
    formData.append('titre', this.currentAppelOffre.titre);
    formData.append('description', this.currentAppelOffre.description);
    formData.append('localisation', this.currentAppelOffre.localisation);
    formData.append('datelimitesoumission', this.datelimitesoumissionFormatted);

    if (this.selectedFile) {
      formData.append('img', this.selectedFile, this.selectedFile.name);
    }

    if (this.selectedDocument) {
      formData.append('document', this.selectedDocument, this.selectedDocument.name);
    }

    if (this.selectedCategorieId) {
      formData.append('categorieId', this.selectedCategorieId);
    }

    this.appeloffreService.updateAppelOffreFormData(this.currentAppelOffre.id, formData).subscribe(
      () => {
        this.toastr.success('Modification terminée avec succès', "Appel d'offre", {
          timeOut: 5000,
          closeButton: true,
          progressBar: true,
          positionClass: 'toast-top-right',
        });
        this.dialogRef.close();
      },
      error => {
        if (error.status === 409) {
          this.toastr.warning('Cannot update AppelOffre as there are related Offres', "Appel d'offre", {
            timeOut: 5000,
            closeButton: true,
            progressBar: true,
            positionClass: 'toast-top-right',
          });
        } else {
          this.toastr.error('Modification échouée', "Appel d'offre", {
            timeOut: 5000,
            closeButton: true,
            progressBar: true,
            positionClass: 'toast-top-right',
          });
        }
        console.error('Error updating currentAppelOffre:', error);
      }
    );
  }

  openFileInput(): void {
    this.showFileInput = true;
  }
}
