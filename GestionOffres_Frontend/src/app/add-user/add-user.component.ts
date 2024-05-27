import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  newUser = new User();
  err : number = 0;
  constructor(private userService : UserService, private router : Router){}
  
  
  ngOnInit(): void {}

  // addUser(){
  //   if (!this.newUser.email) {
  //     return;
  //   }

  //   this.userService.signup(this.newUser)
  //     .subscribe(
  //       response => {
  //         this.router.navigate(['/users']);
  //         console.log('Registration successful', response);
  //         // Handle success, such as redirecting to another page
  //       },
        
  //       error => {
  //         console.error('Registration failed', error);
  //         // Check if the error status is 403 and handle it specifically
  //         if (error.status === 403) {
  //           alert('Saisir un email valid'); // Alert the user for incorrect email format
  //         }
  //         else  if (error.status === 406) {
  //           alert("L'utilisateur existe déjà"); // Alert the user for incorrect email format
  //         } 
  //         else {
  //           alert('Registration failed. Please try again.'); // General error alert
  //         }
  //         this.err = 1; // Set error flag
  //       }
  //     );
  // }

}
