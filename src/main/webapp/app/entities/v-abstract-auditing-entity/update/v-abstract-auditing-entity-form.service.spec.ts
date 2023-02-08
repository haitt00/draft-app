import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-abstract-auditing-entity.test-samples';

import { VAbstractAuditingEntityFormService } from './v-abstract-auditing-entity-form.service';

describe('VAbstractAuditingEntity Form Service', () => {
  let service: VAbstractAuditingEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VAbstractAuditingEntityFormService);
  });

  describe('Service methods', () => {
    describe('createVAbstractAuditingEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVAbstractAuditingEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdTime: expect.any(Object),
            modifiedBy: expect.any(Object),
            modifiedTime: expect.any(Object),
            delFlag: expect.any(Object),
          })
        );
      });

      it('passing IVAbstractAuditingEntity should create a new form with FormGroup', () => {
        const formGroup = service.createVAbstractAuditingEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdTime: expect.any(Object),
            modifiedBy: expect.any(Object),
            modifiedTime: expect.any(Object),
            delFlag: expect.any(Object),
          })
        );
      });
    });

    describe('getVAbstractAuditingEntity', () => {
      it('should return NewVAbstractAuditingEntity for default VAbstractAuditingEntity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVAbstractAuditingEntityFormGroup(sampleWithNewData);

        const vAbstractAuditingEntity = service.getVAbstractAuditingEntity(formGroup) as any;

        expect(vAbstractAuditingEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewVAbstractAuditingEntity for empty VAbstractAuditingEntity initial value', () => {
        const formGroup = service.createVAbstractAuditingEntityFormGroup();

        const vAbstractAuditingEntity = service.getVAbstractAuditingEntity(formGroup) as any;

        expect(vAbstractAuditingEntity).toMatchObject({});
      });

      it('should return IVAbstractAuditingEntity', () => {
        const formGroup = service.createVAbstractAuditingEntityFormGroup(sampleWithRequiredData);

        const vAbstractAuditingEntity = service.getVAbstractAuditingEntity(formGroup) as any;

        expect(vAbstractAuditingEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVAbstractAuditingEntity should not enable id FormControl', () => {
        const formGroup = service.createVAbstractAuditingEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVAbstractAuditingEntity should disable id FormControl', () => {
        const formGroup = service.createVAbstractAuditingEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
