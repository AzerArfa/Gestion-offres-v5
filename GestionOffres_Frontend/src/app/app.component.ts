// app.component.ts
import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router, NavigationEnd } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  @ViewChild(NavbarComponent) navbar!: NavbarComponent;

  title = 'gestionOffres';

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    /* this.authService.loadToken();
     if (this.authService.getToken()==null || this.authService.isTokenExpired()){
      this.router.navigate(['/login']);
    } */
  }

  ngAfterViewInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.navbar.refreshNavbar();
      }
    });
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
