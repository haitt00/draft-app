import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-user.test-samples';

import { VUserFormService } from './v-user-form.service';

describe('VUser Form Service', () => {
  let service: VUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VUserFormService);
  });

  describe('Service methods', () => {
    describe('createVUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            fullname: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            password: expect.any(Object),
            email: expect.any(Object),
            emailVerified: expect.any(Object),
            language: expect.any(Object),
            enabled: expect.any(Object),
            vRoles: expect.any(Object),
            vOrganizations: expect.any(Object),
          })
        );
      });

      it('passing IVUser should create a new form with FormGroup', () => {
        const formGroup = service.createVUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            fullname: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            password: expect.any(Object),
            email: expect.any(Object),
            emailVerified: expect.any(Object),
            language: expect.any(Object),
            enabled: expect.any(Object),
            vRoles: expect.any(Object),
            vOrganizations: expect.any(Object),
          })
        );
      });
    });

    describe('getVUser', () => {
      it('should return NewVUser for default VUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVUserFormGroup(sampleWithNewData);

        const vUser = service.getVUser(formGroup) as any;

        expect(vUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewVUser for empty VUser initial value', () => {
        const formGroup = service.createVUserFormGroup();

        const vUser = service.getVUser(formGroup) as any;

        expect(vUser).toMatchObject({});
      });

      it('should return IVUser', () => {
        const formGroup = service.createVUserFormGroup(sampleWithRequiredData);

        const vUser = service.getVUser(formGroup) as any;

        expect(vUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVUser should not enable id FormControl', () => {
        const formGroup = service.createVUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVUser should disable id FormControl', () => {
        const formGroup = service.createVUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
