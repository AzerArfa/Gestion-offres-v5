import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { SuperAdminGuard } from './superadmin.guard';
import { AuthService } from '../services/auth.service';

describe('SuperAdminGuard', () => {
  let guard: SuperAdminGuard;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated', 'isSuperAdmin']);
    routerSpy = jasmine.createSpyObj('Router', ['createUrlTree']);

    TestBed.configureTestingModule({
      providers: [
        SuperAdminGuard,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    guard = TestBed.inject(SuperAdminGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should allow the super admin access', () => {
    authServiceSpy.isAuthenticated.and.returnValue(true);
    authServiceSpy.isSuperAdmin.and.returnValue(true);
    expect(guard.canActivate(null as any, null as any)).toBeTrue();
  });

  it('should redirect to forbidden when not super admin', () => {
    authServiceSpy.isAuthenticated.and.returnValue(true);
    authServiceSpy.isSuperAdmin.and.returnValue(false);
    expect(guard.canActivate(null as any, null as any)).toEqual(routerSpy.createUrlTree(['/forbidden']));
  });

  it('should redirect to forbidden when not authenticated', () => {
    authServiceSpy.isAuthenticated.and.returnValue(false);
    expect(guard.canActivate(null as any, null as any)).toEqual(routerSpy.createUrlTree(['/forbidden']));
  });
});
