import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-role.test-samples';

import { VRoleFormService } from './v-role-form.service';

describe('VRole Form Service', () => {
  let service: VRoleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VRoleFormService);
  });

  describe('Service methods', () => {
    describe('createVRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            applicationId: expect.any(Object),
            vUsers: expect.any(Object),
            vOrganizations: expect.any(Object),
          })
        );
      });

      it('passing IVRole should create a new form with FormGroup', () => {
        const formGroup = service.createVRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            applicationId: expect.any(Object),
            vUsers: expect.any(Object),
            vOrganizations: expect.any(Object),
          })
        );
      });
    });

    describe('getVRole', () => {
      it('should return NewVRole for default VRole initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVRoleFormGroup(sampleWithNewData);

        const vRole = service.getVRole(formGroup) as any;

        expect(vRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewVRole for empty VRole initial value', () => {
        const formGroup = service.createVRoleFormGroup();

        const vRole = service.getVRole(formGroup) as any;

        expect(vRole).toMatchObject({});
      });

      it('should return IVRole', () => {
        const formGroup = service.createVRoleFormGroup(sampleWithRequiredData);

        const vRole = service.getVRole(formGroup) as any;

        expect(vRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVRole should not enable id FormControl', () => {
        const formGroup = service.createVRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVRole should disable id FormControl', () => {
        const formGroup = service.createVRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
