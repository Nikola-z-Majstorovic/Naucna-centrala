import {Inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GenericService {

  constructor(private http: HttpClient, @Inject('BASE_API_URL') private baseUrl: string) {}

  get<T>(relativeUrl: string): Observable<T> {
    // const params: HttpParams = new HttpParams().set('_id',id);
    return this.http.get<T>(this.baseUrl + relativeUrl);
  }
  put<T>(relativeUrl: string, t: T): Observable<T> {
    const headers: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.put<T>(this.baseUrl + relativeUrl, t, { headers });
  }
}
