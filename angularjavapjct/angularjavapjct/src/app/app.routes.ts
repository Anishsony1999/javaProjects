import { Routes } from '@angular/router';
import { RegisterComponent } from './user/register/register.component';
import { HomeComponent } from './home/home.component';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { AppointmentBookingFormComponent } from './appointment-booking-form/appointment-booking-form.component';


export const routes: Routes = [
    { path: 'user', loadChildren: () => import('./user/user.module').then(m => m.UserModule) },
    // { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: '', component: HomeComponent },
    { path: 'user/dashboard', component: UserDashboardComponent },
  { path: 'doctor/dashboard', component: DoctorDashboardComponent },
  { path: 'book-appointment', component: AppointmentBookingFormComponent },
   
  ];




