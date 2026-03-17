import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private router: Router) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (request.url.startsWith(`${environment.backend_url}/auth`)) {
      return next.handle(request);
    } else if (request.url.endsWith('v4/shorten')) {
      return next.handle(request);
    } else {
      if (this.authService.getToken()) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${this.authService.getToken()}`
          }
        });
      }

      return next.handle(request).pipe(
        catchError((err: HttpErrorResponse) => {
          if (err && err.status === 401) {
            // Login removed: do not redirect to the login page on unauthorized.
            console.warn('Unauthorized request (401) while login flow is disabled.');
          }

          return throwError(err);
        })
      );
    }
  }
}
