import { Component, OnInit } from '@angular/core';
import { Entreprise } from '../model/entreprise.model'; // Ensure path is correct
import { UserService } from '../services/user.service'; // Ensure UserService is correctly imported and has the getAllEntreprises method

@Component({
  selector: 'app-list-entreprises',
  templateUrl: './listentreprises.component.html',
  styleUrls: ['./listentreprises.component.css'] // Assuming you have corresponding CSS
})
export class ListentreprisesComponent implements OnInit {
  entreprises: Entreprise[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getallentreprises().subscribe({
      next: (entreprises) => {
        this.entreprises = entreprises;
      },
      error: (error) => {
        console.error('Failed to load entreprises:', error);
      }
    });
  }
}
