import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from '../register/register.component';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,RegisterComponent,RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  userType: string = 'user';  // Default to 'user'
  isDoctor: boolean = false;

  constructor(private fb: FormBuilder,private router: Router) {
    // Initializing form group
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      userType: ['user', Validators.required]  // userType field to toggle between Doctor and User
    });

    // Listen for changes in the userType field to update form behavior
    this.loginForm.get('userType')?.valueChanges.subscribe(value => {
      this.isDoctor = value === 'doctor';
      this.updateFormForUserType();
    });
  }

  // Update form based on the selected user type
  updateFormForUserType() {
    if (this.isDoctor) {
      // If Doctor login, you can add additional validations or fields
      this.loginForm.get('username')?.setValidators([Validators.required, Validators.minLength(5)]);
    } else {
      // If User login, we can add default validations
      this.loginForm.get('username')?.setValidators([Validators.required]);
    }
    this.loginForm.get('username')?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const userType = this.loginForm.value.userType;
      
      // Based on the userType (either 'user' or 'doctor'), navigate to the respective dashboard
      if (userType === 'user') {
        this.router.navigate(['/user/dashboard']);  // Navigate to User Dashboard
      } else if (userType === 'doctor') {
        this.router.navigate(['/doctor/dashboard']);  // Navigate to Doctor Dashboard
      }
    }
  }
}
