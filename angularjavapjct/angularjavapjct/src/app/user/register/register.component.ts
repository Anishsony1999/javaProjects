import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.registerForm = this.fb.group({
      fullname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      blood_group: ['', Validators.required],
      phone_number: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading = true;
      const userData = this.registerForm.value;
      console.log('Data being sent to backend:', userData); // Log the data to ensure it matches the backend's expected format
      
      this.userService.register(userData).subscribe(
        (response) => {
          console.log('Patient registered successfully:', response);
          this.registerForm.reset();
          this.router.navigate(['/login']);
        },
        (error) => {
          this.isLoading = false;
          console.error('Error registering patient:', error);
          alert('Registration failed! Please try again later.');
        }
      );
    }
  }  
}
