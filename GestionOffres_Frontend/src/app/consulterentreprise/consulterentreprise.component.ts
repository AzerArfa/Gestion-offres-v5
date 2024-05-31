import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user.service';
import { Entreprise } from '../model/entreprise.model';
import { Location } from '@angular/common'; 
@Component({
  selector: 'app-consulterentreprise',
  templateUrl: './consulterentreprise.component.html',
  styleUrls: ['./consulterentreprise.component.css']
})
export class ConsulterentrepriseComponent implements OnInit{
  constructor(private activatedRoute:ActivatedRoute,private userService:UserService,
    private location: Location){}
  identreprise:any;
  currentEntreprise: Entreprise = new Entreprise();
 ngOnInit(): void {
   const identreprise=this.activatedRoute.snapshot.params['id'];
  this.loadCurrentEntreprise(identreprise);
 }
 loadCurrentEntreprise(identreprise: string): void {
  this.userService.getEntrepriseById(identreprise).subscribe(
    entreprise => this.currentEntreprise = entreprise,
    error => console.error('Error loading entreprise data:', error)
  );
}
goBack(): void {
  this.location.back(); // Method to go back
}
}
