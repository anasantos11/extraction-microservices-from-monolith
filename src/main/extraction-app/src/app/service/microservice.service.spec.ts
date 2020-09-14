import { TestBed } from '@angular/core/testing';

import { MicroserviceService } from './microservice.service';

describe('MicroserviceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MicroserviceService = TestBed.get(MicroserviceService);
    expect(service).toBeTruthy();
  });
});
