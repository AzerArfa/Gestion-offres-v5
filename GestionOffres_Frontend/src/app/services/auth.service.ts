import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { authApiURL } from '../config';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedInStatus = new BehaviorSubject<boolean>(this.hasToken());
  private userInfoSubject = new BehaviorSubject<any>(this.getUserInfoFromStorage());

  public isloggedIn = this.loggedInStatus.asObservable();
  public userInfo = this.userInfoSubject.asObservable();

  public isloggedInState: boolean = false;
  public userInfoData: any = null;

  constructor(private http: HttpClient) {
    this.checkLoginStatus(); 
  }
  hasEntreprises(): boolean {
    const userInfo = this.getUserInfo();
    console.log(userInfo.entreprises);
    return userInfo.entreprises;
  }
isAdmin(): boolean {
  const userInfo = this.getUserInfo();
  return userInfo.roles.includes('ROLE_ADMIN');
}
isSuperAdmin(): boolean {
  const userInfo = this.getUserInfo();
  return userInfo.roles.includes('ROLE_SUPERADMIN');
}
isAuthenticated(): boolean {
  const token = localStorage.getItem('authToken'); // or however you store your token
  console.log('Token Present:', !!token);
  return !!token; // Example condition
}

  login(user: any): Observable<HttpResponse<any>> {
    return this.http.post<any>(`${authApiURL}/login`, user, { observe: 'response' }).pipe(
      tap((res: HttpResponse<any>) => {
        
      console.log('Login response:', res.body);
        const token = res.body.jwt;
        if (token) {
          localStorage.setItem('authToken', token);
          this.isloggedInState = true;
          this.loggedInStatus.next(true);

          // Construct userInfo from response
          this.userInfoData = {
            roles: res.body.roles,
            entreprises: res.body.entreprises || [],
            userId: res.body.userId,
            imageUrl: res.body.userImage ? `data:image/png;base64,${res.body.userImage}` : null // Handle null image
          };
          localStorage.setItem('userInfo', JSON.stringify(this.userInfoData));
          this.userInfoSubject.next(this.userInfoData);
        }
      })
    );
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(`${authApiURL}/signup`, user);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getUserInfo(): any {
    const userInfoStr = localStorage.getItem('userInfo');
    if (userInfoStr) {
      try {
        this.userInfoData = JSON.parse(userInfoStr);
        console.log('Parsed user info from localStorage:', this.userInfoData);
      } catch (error) {
        console.error('Error parsing user info from localStorage', error);
        this.userInfoData = null;
      }
    }
    return this.userInfoData;
  }

  logout(): void {
    this.isloggedInState = false;
    console.log('Token removed from localStorage');
    localStorage.removeItem('authToken');
    localStorage.removeItem('userInfo');
    this.loggedInStatus.next(false);
    this.userInfoSubject.next(null);
    this.userInfoData = null;
  }

  // Update user info in local storage and BehaviorSubject
  updateUserInfo(updatedUser: any): void {
    const updatedUserInfo = {
      ...this.userInfoData,
      ...updatedUser,
      entreprises: updatedUser.entreprises || this.userInfoData.entreprises, // Safeguard against undefined entreprises
      imageUrl: updatedUser.body.img ? `data:image/png;base64,${updatedUser.body.img}` : this.userInfoData.imageUrl
    };
    localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo));
    this.userInfoData=updatedUserInfo;
    this.userInfoSubject.next(updatedUserInfo);
  }
  

  // New method to refresh token and user info
  refreshUserInfo(updatedUser: any): void {
    console.log('Refreshing user info with:', updatedUser); // Debugging
    const token = this.getToken();
    if (token) {
      console.log('Current token:', token); // Debugging
      localStorage.setItem('authToken', token); // Assuming the token remains the same
      this.updateUserInfo(updatedUser);
      console.log('Token and user info refreshed.'); // Debugging
    } else {
      console.warn('No token found. Cannot refresh user info.'); // Debugging
    }
  }

  private checkLoginStatus(): void {
    const token = this.getToken();
    this.isloggedInState = !!token;
    if (this.isloggedInState) {
      const userInfoStr = localStorage.getItem('userInfo');
      console.log('Check login status user info string:', userInfoStr);
      if (userInfoStr) {
        try {
          this.userInfoData = JSON.parse(userInfoStr);
          console.log('Check login status user info:', this.userInfoData);
        } catch (error) {
          console.error('Error parsing user info from localStorage', error);
          this.userInfoData = null;
        }
      }
    }
    console.log('Checked login status, isloggedIn:', this.isloggedInState);
  }
  getUserId(): Observable<string | null> {
    return this.userInfo.pipe(
        map(userInfo => userInfo?.userId || null)
    );
}

  private getUserInfoFromStorage(): any {
    return this.getUserInfo();
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }
}

