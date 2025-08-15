import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-appointment-booking-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './appointment-booking-form.component.html',
  styleUrl: './appointment-booking-form.component.css'
})
export class AppointmentBookingFormComponent {

  // Define an object to store the appointment details
  appointment = {
    date: '',
    time: '',
    reason: ''
  };
  todayDate: string = new Date().toISOString().split('T')[0]; 

  constructor(private router : Router) {}

  onSubmit(): void {
    // Log the appointment data on form submission (this could be replaced with API logic)
    console.log('Appointment Booked:', this.appointment);

    // Reset the form after submission (optional)
    this.appointment = {
      date: '',
      time: '',
      reason: ''
    };

    this.goBack();
  }
  goBack() {
    // Navigate back to the dashboard
    this.router.navigate(['/user/dashboard']);
  }

}
