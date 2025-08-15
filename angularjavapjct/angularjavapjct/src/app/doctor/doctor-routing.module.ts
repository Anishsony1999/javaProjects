import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ManageUsersComponent } from './manage-users/manage-users.component';
import { ViewAppointmentsComponent } from './view-appointments/view-appointments.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'manage-users', component: ManageUsersComponent },
  { path: 'view-appointments', component: ViewAppointmentsComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DoctorRoutingModule { }
