import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { BookAppointmentComponent } from './book-appointment/book-appointment.component';

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'book-appointment', component: BookAppointmentComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],  // Use `forChild` since this is a feature module
  exports: [RouterModule]  // Export RouterModule so it can be used in the UserModule
})
export class UserRoutingModule { }
