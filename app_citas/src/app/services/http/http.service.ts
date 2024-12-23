import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import * as CryptoJS from 'crypto-js';

export interface ApiResponse {
  code: number;
  message: string;
  data: any;
  warning: string;
  error: string;
  errors: any;
  warnings: any;
}

export interface ErrorApiResponse extends ApiResponse {
  sideError: string;
}

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  private baseUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getToken(): string {
    return this.decrypt(localStorage.getItem('token') || '');
  }

  private decrypt(data: string): string {
    return CryptoJS.AES.decrypt(data, environment.encryptionKey).toString(CryptoJS.enc.Utf8);
  }

  private handleError(error: HttpErrorResponse) {
    // Verificar si el error es un Blob y el tipo es JSON
    if (error.error instanceof Blob && error.error.type === 'application/json') {
      return new Observable<any>((observer) => {
        const reader = new FileReader();
        reader.onload = () => {
          try {
            // Convertimos el texto a JSON
            const errorText = reader.result as string;
            const errorJson = JSON.parse(errorText);

            // Manejamos el error como siempre
            let errorMessage = `Server-side error: ${error.status} - ${error.statusText}`;
            observer.error({ ...errorJson, sideError: errorMessage } as ErrorApiResponse);
          } catch (e: any) {
            observer.error(`Error parsing JSON: ${e.message}`);
          }
        };
        reader.onerror = () => {
          observer.error('Error reading the error blob.');
        };
        reader.readAsText(error.error);  // Convertir el Blob a texto
      });
    } else {
      // Manejo estándar del error
      let errorMessage = 'Unknown error occurred';
      let errorResponse = error.error as ErrorApiResponse;

      if (error.error instanceof ErrorEvent) {
        // Error del lado del cliente
        errorMessage = `Client-side error: ${error.error.message}`;
      } else {
        // Error del lado del servidor
        errorMessage = `Server-side error: ${error.status} - ${error.message}`;
      }
      return throwError({ ...errorResponse, sideError: errorMessage } as ErrorApiResponse);
    }
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
      Authorization: `Bearer ${this.getToken()}`,
    });
  }

  get<T>(endpoint: string, params?: any, auth: boolean = false, responseType: 'json' | 'blob' = 'json'): Observable<T | Blob> {
    return this.http
      .get<T>(`${this.baseUrl}/${endpoint}`, {
        headers: auth ? this.getAuthHeaders() : this.getHeaders(),
        params: params,
        responseType: responseType === 'blob' ? 'blob' as 'json' : 'json' as 'json'
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  post<T>(endpoint: string, body: any, auth: boolean = false, responseType: 'json' | 'blob' = 'json'): Observable<T | Blob> {
    return this.http
      .post<T | Blob>(`${this.baseUrl}/${endpoint}`, body, {
        headers: auth ? this.getAuthHeaders() : this.getHeaders(),
        responseType: responseType === 'blob' ? 'blob' as 'json' : 'json' as 'json'
      })
      .pipe(catchError(this.handleError));
  }


  put<T>(endpoint: string, body: any, auth: boolean = false): Observable<T> {
    return this.http
      .put<T>(`${this.baseUrl}/${endpoint}`, body, {
        headers: auth ? this.getAuthHeaders() : this.getHeaders(),
      })
      .pipe(catchError(this.handleError));
  }

  patch<T>(endpoint: string, body: any, auth: boolean = false, responseType: 'json' | 'blob' = 'json'): Observable<T> {
    return this.http
      .patch<T>(`${this.baseUrl}/${endpoint}`, body, {
        headers: auth ? this.getAuthHeaders() : this.getHeaders(),
        responseType: responseType === 'blob' ? 'blob' as 'json' : 'json' as 'json'
      })
      .pipe(catchError(this.handleError));
  }

  delete<T>(endpoint: string, auth: boolean = false): Observable<T> {
    return this.http
      .delete<T>(`${this.baseUrl}/${endpoint}`, { headers: auth ? this.getAuthHeaders() : this.getHeaders() })
      .pipe(catchError(this.handleError));
  }
}
