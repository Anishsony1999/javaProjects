import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'https://your-backend-api.com/api/user';  // Replace with your actual user API URL

  private registerUrl = 'http://localhost:8080/doctors/patients';  // Replace with actual register API URL
  constructor(private http: HttpClient) {}

  // Method to get user details
  getUserDetails(): Observable<any> {
    // Pass token from AuthService for authentication
    const token = localStorage.getItem('authToken');
    return this.http.get<any>(this.apiUrl, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
   // Method to register a new user
   register(userData: any): Observable<any> {
    return this.http.post<any>(this.registerUrl, userData);
  }

  getPatients(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  deletePatient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
