import { TestBed } from '@angular/core/testing';

import { MfaSpringServiceService } from './mfa-spring-service.service';

describe('MfaSpringServiceService', () => {
  let service: MfaSpringServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MfaSpringServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
