import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-dashboard.component.html',
  styleUrl: './user-dashboard.component.css'
})
export class UserDashboardComponent {
// Define patient details (these could come from an API in a real application)
patientName: string = "Anu Thomas";
patientEmail: string = "anuthomas@gmail.com";
patientPhone: string = "123-456-7890";
patientBloodGroup: string = "O+";
registeredDate: Date = new Date();



appointments = [
  { id:'001', date: new Date(), time: '10:00 AM',reason:'Fever' },
  { id:'002', date: new Date(), time: '11:00 AM',reason:'Cough'  },
  // Add more appointments as needed
];

constructor(private router : Router) { }

ngOnInit(): void {
  // Fetch patient data and appointments from an API in a real application
}

// Method to book an appointment
bookAppointment(): void {

  this.router.navigate(['/book-appointment'])
  // Logic for booking an appointment (e.g., navigate to a booking page or show a modal)
}

// Method to view appointment details
viewAppointmentDetails(appointment: any): void {
  alert(`Details of appointment on ${appointment.date} at ${appointment.time}: ${appointment.details}`);
  // Logic for viewing appointment details (could open a modal or redirect to a detailed page)
}

logout(){
  this.router.navigate(['/user/login']);
}
}
