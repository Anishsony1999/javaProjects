import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private apiUrl = 'https://your-backend-api.com/api/appointments';  // Replace with your actual appointments API URL

  constructor(private http: HttpClient) {}

  // Method to book an appointment
  bookAppointment(appointmentData: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, appointmentData);
  }

  // Method to get appointments
  getAppointments(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }





  deleteAppointment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
