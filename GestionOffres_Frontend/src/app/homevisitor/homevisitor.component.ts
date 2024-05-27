import { Component } from '@angular/core';
import { AppeloffreService } from '../services/appeloffre.service';
import { Categorie } from '../model/categorie.model';

@Component({
  selector: 'app-homevisitor',
  templateUrl: './homevisitor.component.html',
  styleUrls: ['./homevisitor.component.css']
})
export class HomevisitorComponent {
  appeloffres: any[] = []; // Using any[] to match the service's generic return type
  isLoading = true; // Track loading state
  categories: Categorie[] = [];
  constructor(private appeloffreService: AppeloffreService) {}
  selectedCategoryId: string = '';
  ngOnInit(): void {
    this.loadAllAppelOffres();
    this.fetchCategories();
  }
  fetchCategories(): void {
    this.appeloffreService.getAllCategoriesVISITOR().subscribe({
      next: (categories) => this.categories = categories,
      error: (error) => console.error('Error fetching categories:', error)
    });
  }
  loadAllAppelOffres(): void {
    if (this.selectedCategoryId) {
      this.appeloffreService.getAppelOffresByCategorieIdVISITOR(this.selectedCategoryId).subscribe({
        next: (appeloffres) => {
          this.appeloffres = appeloffres;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Failed to load appeloffres by category:', error);
          this.isLoading = false;
        }
      });
    } else {
      this.appeloffreService.getAllAppelOffresvisitor().subscribe({
        next: (appeloffres) => {
          this.appeloffres = appeloffres;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Failed to load all appeloffres:', error);
          this.isLoading = false;
        }
      });
    }
    
  }
// In your home.component.ts
onCategorySelected(event: Event): void {
  const selectElement = event.target as HTMLSelectElement; // Safe casting
  this.selectedCategoryId = selectElement.value;
  this.loadAllAppelOffres();
}
}
