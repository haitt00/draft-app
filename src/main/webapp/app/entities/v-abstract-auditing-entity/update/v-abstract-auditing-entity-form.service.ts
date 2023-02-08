import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVAbstractAuditingEntity, NewVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVAbstractAuditingEntity for edit and NewVAbstractAuditingEntityFormGroupInput for create.
 */
type VAbstractAuditingEntityFormGroupInput = IVAbstractAuditingEntity | PartialWithRequiredKeyOf<NewVAbstractAuditingEntity>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVAbstractAuditingEntity | NewVAbstractAuditingEntity> = Omit<T, 'createdTime' | 'modifiedTime'> & {
  createdTime?: string | null;
  modifiedTime?: string | null;
};

type VAbstractAuditingEntityFormRawValue = FormValueOf<IVAbstractAuditingEntity>;

type NewVAbstractAuditingEntityFormRawValue = FormValueOf<NewVAbstractAuditingEntity>;

type VAbstractAuditingEntityFormDefaults = Pick<NewVAbstractAuditingEntity, 'id' | 'createdTime' | 'modifiedTime' | 'delFlag'>;

type VAbstractAuditingEntityFormGroupContent = {
  id: FormControl<VAbstractAuditingEntityFormRawValue['id'] | NewVAbstractAuditingEntity['id']>;
  createdBy: FormControl<VAbstractAuditingEntityFormRawValue['createdBy']>;
  createdTime: FormControl<VAbstractAuditingEntityFormRawValue['createdTime']>;
  modifiedBy: FormControl<VAbstractAuditingEntityFormRawValue['modifiedBy']>;
  modifiedTime: FormControl<VAbstractAuditingEntityFormRawValue['modifiedTime']>;
  delFlag: FormControl<VAbstractAuditingEntityFormRawValue['delFlag']>;
};

export type VAbstractAuditingEntityFormGroup = FormGroup<VAbstractAuditingEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VAbstractAuditingEntityFormService {
  createVAbstractAuditingEntityFormGroup(
    vAbstractAuditingEntity: VAbstractAuditingEntityFormGroupInput = { id: null }
  ): VAbstractAuditingEntityFormGroup {
    const vAbstractAuditingEntityRawValue = this.convertVAbstractAuditingEntityToVAbstractAuditingEntityRawValue({
      ...this.getFormDefaults(),
      ...vAbstractAuditingEntity,
    });
    return new FormGroup<VAbstractAuditingEntityFormGroupContent>({
      id: new FormControl(
        { value: vAbstractAuditingEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdBy: new FormControl(vAbstractAuditingEntityRawValue.createdBy),
      createdTime: new FormControl(vAbstractAuditingEntityRawValue.createdTime),
      modifiedBy: new FormControl(vAbstractAuditingEntityRawValue.modifiedBy),
      modifiedTime: new FormControl(vAbstractAuditingEntityRawValue.modifiedTime),
      delFlag: new FormControl(vAbstractAuditingEntityRawValue.delFlag),
    });
  }

  getVAbstractAuditingEntity(form: VAbstractAuditingEntityFormGroup): IVAbstractAuditingEntity | NewVAbstractAuditingEntity {
    return this.convertVAbstractAuditingEntityRawValueToVAbstractAuditingEntity(
      form.getRawValue() as VAbstractAuditingEntityFormRawValue | NewVAbstractAuditingEntityFormRawValue
    );
  }

  resetForm(form: VAbstractAuditingEntityFormGroup, vAbstractAuditingEntity: VAbstractAuditingEntityFormGroupInput): void {
    const vAbstractAuditingEntityRawValue = this.convertVAbstractAuditingEntityToVAbstractAuditingEntityRawValue({
      ...this.getFormDefaults(),
      ...vAbstractAuditingEntity,
    });
    form.reset(
      {
        ...vAbstractAuditingEntityRawValue,
        id: { value: vAbstractAuditingEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VAbstractAuditingEntityFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdTime: currentTime,
      modifiedTime: currentTime,
      delFlag: false,
    };
  }

  private convertVAbstractAuditingEntityRawValueToVAbstractAuditingEntity(
    rawVAbstractAuditingEntity: VAbstractAuditingEntityFormRawValue | NewVAbstractAuditingEntityFormRawValue
  ): IVAbstractAuditingEntity | NewVAbstractAuditingEntity {
    return {
      ...rawVAbstractAuditingEntity,
      createdTime: dayjs(rawVAbstractAuditingEntity.createdTime, DATE_TIME_FORMAT),
      modifiedTime: dayjs(rawVAbstractAuditingEntity.modifiedTime, DATE_TIME_FORMAT),
    };
  }

  private convertVAbstractAuditingEntityToVAbstractAuditingEntityRawValue(
    vAbstractAuditingEntity: IVAbstractAuditingEntity | (Partial<NewVAbstractAuditingEntity> & VAbstractAuditingEntityFormDefaults)
  ): VAbstractAuditingEntityFormRawValue | PartialWithRequiredKeyOf<NewVAbstractAuditingEntityFormRawValue> {
    return {
      ...vAbstractAuditingEntity,
      createdTime: vAbstractAuditingEntity.createdTime ? vAbstractAuditingEntity.createdTime.format(DATE_TIME_FORMAT) : undefined,
      modifiedTime: vAbstractAuditingEntity.modifiedTime ? vAbstractAuditingEntity.modifiedTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
