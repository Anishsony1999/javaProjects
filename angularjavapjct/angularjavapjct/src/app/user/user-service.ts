import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/api/patients'; // Backend URL for patient registration

  constructor(private http: HttpClient) {}

  // Register method sends a POST request with JSON data
  register(userData: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, userData, {
      headers: { 'Content-Type': 'application/json' } // Sending data as JSON
    });
  }
}
