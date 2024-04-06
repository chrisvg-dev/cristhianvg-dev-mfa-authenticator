import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { MfaSpringServiceService } from './services/mfa-spring-service.service';
import { Observable } from 'rxjs';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  qrErrorMessage = '';
  base64Image: string|null = null;

  enableMFAForm = this.fb.group({
    firstCode: ['', Validators.required],
    secondCode: ['', Validators.required]
  });

  constructor(private mfaService: MfaSpringServiceService, private fb: FormBuilder) {}
  ngOnInit(): void {
    this.loadQRCode();
  }

  sendValidationAndEnableRequest(): void {
    this.enableMFAForm.markAllAsTouched();
    if (this.enableMFAForm.valid) {
      this.mfaService.validateAndEnableMFA(this.enableMFAForm.value).then((response: Observable<any>) => {
        response.subscribe({
          next: data => {
            console.log(data);
          },
          error: error => {
            console.log(error);
          },
          complete: () => {
            console.log('complete');
          }
        })
      });
    }
  }

  loadQRCode(): void {
    this.mfaService.loadQRCode()
      .then((response: Observable<any>) => {
        response.subscribe({
          next: data => {
            console.log(data);
            
            if (data.status !== undefined && !data.status) {
              this.base64Image = null;
              this.qrErrorMessage = data.message;
            } else {
              this.base64Image = 'data:image/png;base64,'+data.qrCode;
            }
          },
          error: error => {
            console.log(error);
          },
          complete: () => {
            console.log('complete');
          }
        })
      }).catch(error => {
      console.error(error);
    });
  }
}
