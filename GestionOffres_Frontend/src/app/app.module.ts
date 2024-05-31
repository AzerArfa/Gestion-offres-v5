import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { FooterComponent } from './footer/footer.component';
import { UsersComponent } from './users/users.component';
import { LoginComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';
import { AddUserComponent } from './add-user/add-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { SignUpComponent } from './sign-up/sign-up.component';

import { RecaptchaModule } from 'ng-recaptcha';
import { ProfileComponent } from './profile/profile.component';
import { RechercheUsersComponent } from './recherche-users/recherche-users.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { UserService } from './services/user.service';
import { AuthInterceptor } from './services/auth.interceptor';
import { NavbarComponent } from './navbar/navbar.component';
import { AddentrepriseComponent } from './addentreprise/addentreprise.component';
import { UpdateEntrepriseComponent } from './update-entreprise/update-entreprise.component';
import { AddappeloffreComponent } from './addappeloffre/addappeloffre.component';
import { DetailsappeloffreComponent } from './detailsappeloffre/detailsappeloffre.component';
import { AppeloffresadminComponent } from './appeloffresadmin/appeloffresadmin.component';
import { UpdateappeloffreComponent } from './updateappeloffre/updateappeloffre.component';
import { AddoffreComponent } from './addoffre/addoffre.component';
import { DetailsappeloffreadminComponent } from './detailsappeloffreadmin/detailsappeloffreadmin.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { ActionentrepriseComponent } from './actionentreprise/actionentreprise.component';
import { DemandecreationentrepriseComponent } from './demandecreationentreprise/demandecreationentreprise.component';
import { DemanderejointentrepriseComponent } from './demanderejointentreprise/demanderejointentreprise.component';
import { HomevisitorComponent } from './homevisitor/homevisitor.component';
import { ListentreprisesComponent } from './listentreprises/listentreprises.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { ToastrModule } from 'ngx-toastr';
import { GestioncategoriesComponent } from './gestioncategories/gestioncategories.component';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';

import { MatListModule } from '@angular/material/list';
import { MatBadgeModule } from '@angular/material/badge';
import { ConsulterentrepriseComponent } from './consulterentreprise/consulterentreprise.component';
import { NotificationslistdialogComponent } from './notificationslistdialog/notificationslistdialog.component';
import { RequestDialogComponent } from './request-dialog/request-dialog.component';
import { EntrepriseRequestDialogComponent } from './entreprise-request-dialog/entreprise-request-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    FooterComponent,
    UsersComponent,
    LoginComponent,
    AddUserComponent,
    UpdateUserComponent,
    SignUpComponent,
    ProfileComponent,
    RechercheUsersComponent,
    NavbarComponent,
    AddentrepriseComponent,
    UpdateEntrepriseComponent,
    AddappeloffreComponent,
    DetailsappeloffreComponent,
    AppeloffresadminComponent,
    UpdateappeloffreComponent,
    AddoffreComponent,
    DetailsappeloffreadminComponent,
    ForbiddenComponent,
    ActionentrepriseComponent,
    DemandecreationentrepriseComponent,
    DemanderejointentrepriseComponent,
    HomevisitorComponent,
    ListentreprisesComponent,
    GestioncategoriesComponent,
    ConsulterentrepriseComponent,
    NotificationslistdialogComponent,
    RequestDialogComponent,
    EntrepriseRequestDialogComponent
    
  ],
  imports: [ToastrModule.forRoot({
    timeOut: 10000,
    positionClass: 'toast-bottom-right',
    preventDuplicates: true,
  }),
  
  MatMenuModule,
  MatBadgeModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    RecaptchaModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatSnackBarModule,
    MatButtonModule,
     MatListModule,
    MatIconModule
  ],
  providers: [
    HttpClient,
    UserService,
    
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent],

})
export class AppModule { }