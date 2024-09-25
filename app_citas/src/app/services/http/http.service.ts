import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  private baseUrl: string = 'https://localhost:8080'; // Cambia por tu URL base

  constructor(private http: HttpClient, private authService: AuthService) { }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Unknown error occurred';
    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // Error del lado del servidor
      errorMessage = `Server-side error: ${error.status} - ${error.message}`;
    }
    return throwError(errorMessage);
  }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      // Agrega más headers si es necesario
    });
  }

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${this.authService.getToken()}`,
    });
  }

  get<T>(endpoint: string, params?: any, auth: boolean = false): Observable<T> {
    return this.http
      .get<T>(`${this.baseUrl}/${endpoint}`, {
        headers: auth ? this.getAuthHeaders() : this.getHeaders(),
        params: params,
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  post<T>(endpoint: string, body: any, auth: boolean = false): Observable<T> {
    return this.http
      .post<T>(`${this.baseUrl}/${endpoint}`, body, { headers: auth ? this.getAuthHeaders() : this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  put<T>(endpoint: string, body: any, auth: boolean = false): Observable<T> {
    return this.http
      .put<T>(`${this.baseUrl}/${endpoint}`, body, { headers: auth ? this.getAuthHeaders() : this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  patch<T>(endpoint: string, body: any, auth: boolean = false): Observable<T> {
    return this.http
      .patch<T>(`${this.baseUrl}/${endpoint}`, body, { headers: auth ? this.getAuthHeaders() : this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  delete<T>(endpoint: string, auth: boolean = false): Observable<T> {
    return this.http
      .delete<T>(`${this.baseUrl}/${endpoint}`, { headers: auth ? this.getAuthHeaders() : this.getHeaders() })
      .pipe(catchError(this.handleError));
  }
}
