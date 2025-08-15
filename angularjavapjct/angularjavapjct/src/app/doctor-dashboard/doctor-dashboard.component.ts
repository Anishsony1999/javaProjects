import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { AppointmentService } from '../services/appointment.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LoginComponent } from '../user/login/login.component';

@Component({
  selector: 'app-doctor-dashboard',
  standalone: true,
  imports: [CommonModule,LoginComponent],
  templateUrl: './doctor-dashboard.component.html',
  styleUrl: './doctor-dashboard.component.css'
})
export class DoctorDashboardComponent {
  selectedPatient: any = null;
  constructor(private router:Router){

  }

  appointments = [
    { patientName: 'Alice Brown', date: new Date('2024-11-20'), time: '10:00 AM',reason:'Fever' },
    { patientName: 'Bob Smith', date: new Date('2024-12-01'), time: '11:00 AM',reason:'Cough'  },
    // Add more appointments as needed
  ];

  patients = [
    { name: 'Alice Brown', age: 45, lastVisit: new Date('2024-10-15'), nextAppointment: new Date('2024-11-20') ,},
    { name: 'Bob Smith', age: 52, lastVisit: new Date('2024-09-10'), nextAppointment: new Date('2024-12-01'),},
    // Add more patient details as needed
  ];
  editAppointment(appointment: any) {
    // Code to edit appointment
  }
  
  deleteAppointment(appointment: any) {
    // Code to delete appointment
  }
  
  viewAppointment(appointment: any) {
    // Code to view appointment details
  }
  
  editPatient(patient: any) {
    // Code to edit patient details
  }
  
  deletePatient(patient: any) {
    // Code to delete patient
  }
  
  viewPatient(patient: any) {
    console.log("inside func..");
    
    // Code to view patient details
    this.selectedPatient = patient;  // Assign selected patient data
  }

  logout(){
    console.log("inside fun");
    
    this.router.navigate(['/user/login']);

  }
 

  

  closeModal() {
    this.selectedPatient = null;  // Reset selected patient to close modal
  }
}

