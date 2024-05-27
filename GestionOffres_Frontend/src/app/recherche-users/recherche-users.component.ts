import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-recherche-users',
  templateUrl: './recherche-users.component.html',
  styleUrls: ['./recherche-users.component.css']
})
export class RechercheUsersComponent implements OnInit {

  nomUser! :string;
  users! : User[];
  allUsers! : User[];
  searchTerm!: string;


  constructor(private userService : UserService){}


  ngOnInit(): void {
    /* 
    this.userService.listeUsers().subscribe(users => {
      console.log(users);
      this.allUsers = users;
      }); */
  }

  onKeyUp(filterText : string){
    this.users = this.allUsers.filter(item =>
    item.name?.toLowerCase().includes(filterText));
    }

  /* rechercherJouers(){
    this.userService.rechercherParNom(this.nomUser).
      subscribe(jouers => {
    this.jouers = jouers; 
    console.log(jouers)});
  } */

}
