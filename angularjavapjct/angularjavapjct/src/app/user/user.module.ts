import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module'; // Import the routing module for user module
import { RegisterComponent } from './register/register.component'; // RegisterComponent declaration
import { LoginComponent } from './login/login.component'; // LoginComponent declaration
import { BookAppointmentComponent } from './book-appointment/book-appointment.component'; // BookAppointmentComponent declaration
import { ReactiveFormsModule } from '@angular/forms';  // Import ReactiveFormsModule to work with reactive forms

@NgModule({
  declarations: [
    RegisterComponent,  // Declare the RegisterComponent here
    LoginComponent,     // Declare the LoginComponent here
    BookAppointmentComponent  // Declare the BookAppointmentComponent here (if needed)
  ],
  imports: [
    CommonModule,       // Import Angular's CommonModule
    UserRoutingModule,  // Import the UserRoutingModule for routing inside this module
    ReactiveFormsModule // Import ReactiveFormsModule to use ReactiveForms
  ]
})
export class UserModule { }
