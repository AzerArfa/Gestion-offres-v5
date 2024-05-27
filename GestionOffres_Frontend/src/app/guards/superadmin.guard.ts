import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';  // Adjust the import path as needed.

@Injectable({
  providedIn: 'root'
})
export class SuperAdminGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    console.log('Authenticated:', this.authService.isAuthenticated());
    console.log('Admin:', this.authService.isAdmin());
    console.log('Super Admin:', this.authService.isSuperAdmin());
  
    if (this.authService.isAuthenticated() && this.authService.isSuperAdmin()) {
      return true;
    } else {
      console.log('Redirecting to forbidden');
      return this.router.createUrlTree(['/forbidden']);
    }
  }
  
}
