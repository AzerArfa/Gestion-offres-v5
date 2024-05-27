import { Component, OnInit } from '@angular/core';
import { AppeloffreService } from '../services/appeloffre.service';
import { UserService } from '../services/user.service';  // Make sure you import UserService
import { AppelOffre } from '../model/appeloffre.model';
import { AuthService } from '../services/auth.service';
import { User } from '../model/user.model';  // Assuming you have a User model with a structure that includes entreprises
import { Categorie } from '../model/categorie.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  appeloffres: AppelOffre[] = [];
  isLoading = true; // Track loading state
  hasEntreprises: boolean = false;
  categories: Categorie[] = [];
  selectedCategoryId: string = '';
  constructor(
    private appeloffreService: AppeloffreService,
    private authService: AuthService,
    private userService: UserService  // Inject UserService
  ) {}

  ngOnInit(): void {
    this.fetchCategories();
    this.loadUserDetails();
    this.loadAllAppelOffres();
    this.hasEntreprises=this.authService.hasEntreprises();
  }

  fetchCategories(): void {
    this.appeloffreService.getAllCategories().subscribe({
      next: (categories) => this.categories = categories,
      error: (error) => console.error('Error fetching categories:', error)
    });
  }
  private loadUserDetails(): void {
    this.authService.getUserId().subscribe(userId => {
      if (userId) {
        this.userService.getUserById(userId).subscribe({
          next: (user: User) => {
            this.hasEntreprises = Array.isArray(user.entreprises) && user.entreprises.length > 0;
          },
          error: (error) => {
            console.error('Failed to fetch user data:', error);
            this.hasEntreprises = false;  // Set to false or handle appropriately
          }
        });
      } else {
        console.error('No user ID found');
        this.hasEntreprises = false;  // Handle situation where no user ID is available
      }
    });
  }

  loadAllAppelOffres(): void {
    if (this.selectedCategoryId) {
      this.appeloffreService.getAppelOffresByCategorieId(this.selectedCategoryId).subscribe({
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
      this.appeloffreService.getAllAppelOffresuser().subscribe({
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
