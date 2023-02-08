import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVTenant, NewVTenant } from '../v-tenant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVTenant for edit and NewVTenantFormGroupInput for create.
 */
type VTenantFormGroupInput = IVTenant | PartialWithRequiredKeyOf<NewVTenant>;

type VTenantFormDefaults = Pick<NewVTenant, 'id' | 'status'>;

type VTenantFormGroupContent = {
  id: FormControl<IVTenant['id'] | NewVTenant['id']>;
  code: FormControl<IVTenant['code']>;
  name: FormControl<IVTenant['name']>;
  description: FormControl<IVTenant['description']>;
  status: FormControl<IVTenant['status']>;
};

export type VTenantFormGroup = FormGroup<VTenantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VTenantFormService {
  createVTenantFormGroup(vTenant: VTenantFormGroupInput = { id: null }): VTenantFormGroup {
    const vTenantRawValue = {
      ...this.getFormDefaults(),
      ...vTenant,
    };
    return new FormGroup<VTenantFormGroupContent>({
      id: new FormControl(
        { value: vTenantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(vTenantRawValue.code),
      name: new FormControl(vTenantRawValue.name),
      description: new FormControl(vTenantRawValue.description),
      status: new FormControl(vTenantRawValue.status),
    });
  }

  getVTenant(form: VTenantFormGroup): IVTenant | NewVTenant {
    return form.getRawValue() as IVTenant | NewVTenant;
  }

  resetForm(form: VTenantFormGroup, vTenant: VTenantFormGroupInput): void {
    const vTenantRawValue = { ...this.getFormDefaults(), ...vTenant };
    form.reset(
      {
        ...vTenantRawValue,
        id: { value: vTenantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VTenantFormDefaults {
    return {
      id: null,
      status: false,
    };
  }
}
