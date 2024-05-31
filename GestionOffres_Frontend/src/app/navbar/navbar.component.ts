import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from '../services/user.service';
import { Entreprise } from '../model/entreprise.model';
import { Notification } from '../model/notification.model';
import { AppeloffreService } from '../services/appeloffre.service';
import { AppelOffre } from '../model/appeloffre.model';
import { MatDialog } from '@angular/material/dialog';
import { NotificationslistdialogComponent } from '../notificationslistdialog/notificationslistdialog.component'; // Import the dialog component

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  userInfo: any;
  isloggedIn: boolean = false;
  isSuperAdmin: boolean = false;
  notifications: Notification[] = [];
  private userInfoSubscription?: Subscription;
  private userIdSubscription?: Subscription;
  sortOrder: string = 'latest';

  constructor(
    public authService: AuthService, 
    private router: Router, 
    private userService: UserService,
    private appelOffreService: AppeloffreService,  // Inject the AppelOffreService
    private cdr: ChangeDetectorRef,
    public dialog: MatDialog // Inject MatDialog
  ) {}

  ngOnInit(): void {
    this.refreshNavbar();
    // Subscribe to changes in user info and login status
    this.userInfoSubscription = this.authService.userInfo.subscribe(userInfo => {
      this.userInfo = userInfo;
      console.log('Navbar user info updated:', this.userInfo);
      this.cdr.detectChanges(); // Manually trigger change detection
    });
  
    this.authService.isloggedIn.subscribe(isLoggedIn => {
      this.isloggedIn = isLoggedIn;
      this.cdr.detectChanges(); // Manually trigger change detection
      if (isLoggedIn) {
        this.notifications = [];  // Clear notifications array
        this.fetchNotifications();
      }
    });
  
    // Initialize with current state
    this.userInfo = this.authService.getUserInfo();
    this.isloggedIn = this.authService.isloggedInState;
    this.isSuperAdmin = this.authService.isSuperAdmin();
    
    if (this.isloggedIn) {
      this.notifications = [];  // Clear notifications array
      this.fetchNotifications();
    }
  }
  

  refreshNavbar(): void {
    this.userInfo = this.authService.getUserInfo();
    this.isloggedIn = this.authService.isloggedInState;
    this.isSuperAdmin = this.authService.isSuperAdmin(); 
    console.log('Navbar refreshed:', this.userInfo);
    this.cdr.detectChanges(); // Manually trigger change detection
  }

  fetchNotifications(): void {
    this.notifications = [];  // Clear notifications array
    this.userIdSubscription = this.authService.getUserId().subscribe(userId => {
      if (userId) {
        this.userService.getUserById(userId).subscribe({
          next: (user: any) => {
            if (user && user.entreprises) {
              user.entreprises.forEach((entreprise: Entreprise) => {
                console.log("CATEGORIEID:", entreprise.idcategorie);
                this.userService.getNotifications(entreprise.idcategorie, entreprise.id).subscribe({
                  next: (notifications: Notification[]) => {
                    notifications.forEach(notification => {
                      if (!this.notifications.some(existingNotification => existingNotification.id === notification.id)) {
                        this.notifications.push(notification);
                        // Fetch the AppelOffre details
                        this.appelOffreService.getAppelOffreById(notification.id_appeloffre).subscribe({
                          next: (appelOffre: AppelOffre) => {
                            notification.message = `Un appel d'offre nommé "${appelOffre.titre}" qui pourrait vous intéresser a été publié.`;
                            this.sortNotifications(); // Sort notifications after fetching all data
                            this.cdr.detectChanges(); // Manually trigger change detection
                          },
                          error: (err: any) => {
                            console.error('Error fetching AppelOffre details:', err);
                          }
                        });
                      }
                    });
                    this.sortNotifications(); // Sort notifications after fetching all data
                    this.cdr.detectChanges(); // Manually trigger change detection
                  },
                  error: (err: any) => {
                    console.error('Error fetching notifications:', err);
                  }
                });
              });
            }
          },
          error: (err: any) => {
            console.error('Error fetching user data:', err);
          }
        });
      }
    });
  }
  

  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'latest' ? 'earliest' : 'latest';
    this.sortNotifications();
  }

  sortNotifications(): void {
    this.notifications.sort((a, b) => {
      if (this.sortOrder === 'latest') {
        return new Date(b.creationDate).getTime() - new Date(a.creationDate).getTime();
      } else {
        return new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime();
      }
    });
    this.cdr.detectChanges();
  }

  onLogout() {
    this.authService.logout();  // Assuming logout method clears the local storage or session
    this.isloggedIn = false;
    this.isSuperAdmin = false;  // Reset superadmin role
    this.router.navigate(['/login']);  // Redirect to login page or home page after logout
  }

  openAllNotificationsDialog(): void {
    this.dialog.open(NotificationslistdialogComponent, {
      width: '500px',
      
      data: { notifications: this.notifications }
    });
  }

  ngOnDestroy(): void {
    if (this.userInfoSubscription) {
      this.userInfoSubscription.unsubscribe();
    }
    if (this.userIdSubscription) {
      this.userIdSubscription.unsubscribe();
    }
  }
}
