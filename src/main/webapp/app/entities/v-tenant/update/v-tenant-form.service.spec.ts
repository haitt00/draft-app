import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-tenant.test-samples';

import { VTenantFormService } from './v-tenant-form.service';

describe('VTenant Form Service', () => {
  let service: VTenantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VTenantFormService);
  });

  describe('Service methods', () => {
    describe('createVTenantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVTenantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
          })
        );
      });

      it('passing IVTenant should create a new form with FormGroup', () => {
        const formGroup = service.createVTenantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
          })
        );
      });
    });

    describe('getVTenant', () => {
      it('should return NewVTenant for default VTenant initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVTenantFormGroup(sampleWithNewData);

        const vTenant = service.getVTenant(formGroup) as any;

        expect(vTenant).toMatchObject(sampleWithNewData);
      });

      it('should return NewVTenant for empty VTenant initial value', () => {
        const formGroup = service.createVTenantFormGroup();

        const vTenant = service.getVTenant(formGroup) as any;

        expect(vTenant).toMatchObject({});
      });

      it('should return IVTenant', () => {
        const formGroup = service.createVTenantFormGroup(sampleWithRequiredData);

        const vTenant = service.getVTenant(formGroup) as any;

        expect(vTenant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVTenant should not enable id FormControl', () => {
        const formGroup = service.createVTenantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVTenant should disable id FormControl', () => {
        const formGroup = service.createVTenantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
