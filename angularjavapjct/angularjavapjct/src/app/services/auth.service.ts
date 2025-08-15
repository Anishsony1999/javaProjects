import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'https://your-backend-api.com/api/auth/login';  // Replace with your actual API URL

  constructor(private http: HttpClient) {}

  // Method to login and save the token
  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(this.apiUrl, { username, password });
  }

  // Save token to local storage or session storage
  saveToken(token: string) {
    localStorage.setItem('authToken', token);
  }

  // Retrieve the token from local storage
  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  // Check if user is logged in by checking token
  isLoggedIn(): boolean {
    return !!this.getToken();  // Returns true if token exists
  }

  // Logout and remove token
  logout() {
    localStorage.removeItem('authToken');
  }
}

