import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authToken = this.authService.getToken();

    console.log('Token added to request headers:', authToken); // Log the token added to the request
    if (authToken) {
      // Correctly format the Authorization header with the 'Bearer ' prefix
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`  // Ensure to include 'Bearer ' prefix here
        }
      });
      return next.handle(authReq);
    }
    return next.handle(req);
  }
}
