import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-permission.test-samples';

import { VPermissionFormService } from './v-permission-form.service';

describe('VPermission Form Service', () => {
  let service: VPermissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VPermissionFormService);
  });

  describe('Service methods', () => {
    describe('createVPermissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVPermissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parentId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            order: expect.any(Object),
            url: expect.any(Object),
            component: expect.any(Object),
            perms: expect.any(Object),
          })
        );
      });

      it('passing IVPermission should create a new form with FormGroup', () => {
        const formGroup = service.createVPermissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parentId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            order: expect.any(Object),
            url: expect.any(Object),
            component: expect.any(Object),
            perms: expect.any(Object),
          })
        );
      });
    });

    describe('getVPermission', () => {
      it('should return NewVPermission for default VPermission initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVPermissionFormGroup(sampleWithNewData);

        const vPermission = service.getVPermission(formGroup) as any;

        expect(vPermission).toMatchObject(sampleWithNewData);
      });

      it('should return NewVPermission for empty VPermission initial value', () => {
        const formGroup = service.createVPermissionFormGroup();

        const vPermission = service.getVPermission(formGroup) as any;

        expect(vPermission).toMatchObject({});
      });

      it('should return IVPermission', () => {
        const formGroup = service.createVPermissionFormGroup(sampleWithRequiredData);

        const vPermission = service.getVPermission(formGroup) as any;

        expect(vPermission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVPermission should not enable id FormControl', () => {
        const formGroup = service.createVPermissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVPermission should disable id FormControl', () => {
        const formGroup = service.createVPermissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
