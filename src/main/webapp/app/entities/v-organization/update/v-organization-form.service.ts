import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVOrganization, NewVOrganization } from '../v-organization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVOrganization for edit and NewVOrganizationFormGroupInput for create.
 */
type VOrganizationFormGroupInput = IVOrganization | PartialWithRequiredKeyOf<NewVOrganization>;

type VOrganizationFormDefaults = Pick<NewVOrganization, 'id' | 'enabled' | 'vUsers' | 'vRoles'>;

type VOrganizationFormGroupContent = {
  id: FormControl<IVOrganization['id'] | NewVOrganization['id']>;
  parentId: FormControl<IVOrganization['parentId']>;
  code: FormControl<IVOrganization['code']>;
  name: FormControl<IVOrganization['name']>;
  description: FormControl<IVOrganization['description']>;
  path: FormControl<IVOrganization['path']>;
  fullPath: FormControl<IVOrganization['fullPath']>;
  enabled: FormControl<IVOrganization['enabled']>;
  type: FormControl<IVOrganization['type']>;
  vUsers: FormControl<IVOrganization['vUsers']>;
  vRoles: FormControl<IVOrganization['vRoles']>;
};

export type VOrganizationFormGroup = FormGroup<VOrganizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VOrganizationFormService {
  createVOrganizationFormGroup(vOrganization: VOrganizationFormGroupInput = { id: null }): VOrganizationFormGroup {
    const vOrganizationRawValue = {
      ...this.getFormDefaults(),
      ...vOrganization,
    };
    return new FormGroup<VOrganizationFormGroupContent>({
      id: new FormControl(
        { value: vOrganizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      parentId: new FormControl(vOrganizationRawValue.parentId),
      code: new FormControl(vOrganizationRawValue.code),
      name: new FormControl(vOrganizationRawValue.name),
      description: new FormControl(vOrganizationRawValue.description),
      path: new FormControl(vOrganizationRawValue.path),
      fullPath: new FormControl(vOrganizationRawValue.fullPath),
      enabled: new FormControl(vOrganizationRawValue.enabled),
      type: new FormControl(vOrganizationRawValue.type),
      vUsers: new FormControl(vOrganizationRawValue.vUsers ?? []),
      vRoles: new FormControl(vOrganizationRawValue.vRoles ?? []),
    });
  }

  getVOrganization(form: VOrganizationFormGroup): IVOrganization | NewVOrganization {
    return form.getRawValue() as IVOrganization | NewVOrganization;
  }

  resetForm(form: VOrganizationFormGroup, vOrganization: VOrganizationFormGroupInput): void {
    const vOrganizationRawValue = { ...this.getFormDefaults(), ...vOrganization };
    form.reset(
      {
        ...vOrganizationRawValue,
        id: { value: vOrganizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VOrganizationFormDefaults {
    return {
      id: null,
      enabled: false,
      vUsers: [],
      vRoles: [],
    };
  }
}
