import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-organization.test-samples';

import { VOrganizationFormService } from './v-organization-form.service';

describe('VOrganization Form Service', () => {
  let service: VOrganizationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VOrganizationFormService);
  });

  describe('Service methods', () => {
    describe('createVOrganizationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVOrganizationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parentId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            path: expect.any(Object),
            fullPath: expect.any(Object),
            enabled: expect.any(Object),
            type: expect.any(Object),
            vUsers: expect.any(Object),
            vRoles: expect.any(Object),
          })
        );
      });

      it('passing IVOrganization should create a new form with FormGroup', () => {
        const formGroup = service.createVOrganizationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parentId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            path: expect.any(Object),
            fullPath: expect.any(Object),
            enabled: expect.any(Object),
            type: expect.any(Object),
            vUsers: expect.any(Object),
            vRoles: expect.any(Object),
          })
        );
      });
    });

    describe('getVOrganization', () => {
      it('should return NewVOrganization for default VOrganization initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVOrganizationFormGroup(sampleWithNewData);

        const vOrganization = service.getVOrganization(formGroup) as any;

        expect(vOrganization).toMatchObject(sampleWithNewData);
      });

      it('should return NewVOrganization for empty VOrganization initial value', () => {
        const formGroup = service.createVOrganizationFormGroup();

        const vOrganization = service.getVOrganization(formGroup) as any;

        expect(vOrganization).toMatchObject({});
      });

      it('should return IVOrganization', () => {
        const formGroup = service.createVOrganizationFormGroup(sampleWithRequiredData);

        const vOrganization = service.getVOrganization(formGroup) as any;

        expect(vOrganization).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVOrganization should not enable id FormControl', () => {
        const formGroup = service.createVOrganizationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVOrganization should disable id FormControl', () => {
        const formGroup = service.createVOrganizationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
