import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const URL: string = 'http://localhost:9090/api/v1/mfa'

@Injectable({
  providedIn: 'root'
})
export class MfaSpringServiceService {

  constructor(private httpClient: HttpClient) {}

  async loadQRCode():Promise<Observable<any>> {
    return await this.httpClient.get<any>(URL + '/qrcode');
  }

  async validateAndEnableMFA(data: any):Promise<Observable<any>> {
    return await this.httpClient.post<any>(URL + '/validateAndEnable', data);
  }
}
