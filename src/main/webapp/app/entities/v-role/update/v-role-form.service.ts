import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVRole, NewVRole } from '../v-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVRole for edit and NewVRoleFormGroupInput for create.
 */
type VRoleFormGroupInput = IVRole | PartialWithRequiredKeyOf<NewVRole>;

type VRoleFormDefaults = Pick<NewVRole, 'id' | 'vUsers' | 'vOrganizations'>;

type VRoleFormGroupContent = {
  id: FormControl<IVRole['id'] | NewVRole['id']>;
  code: FormControl<IVRole['code']>;
  name: FormControl<IVRole['name']>;
  description: FormControl<IVRole['description']>;
  applicationId: FormControl<IVRole['applicationId']>;
  vUsers: FormControl<IVRole['vUsers']>;
  vOrganizations: FormControl<IVRole['vOrganizations']>;
};

export type VRoleFormGroup = FormGroup<VRoleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VRoleFormService {
  createVRoleFormGroup(vRole: VRoleFormGroupInput = { id: null }): VRoleFormGroup {
    const vRoleRawValue = {
      ...this.getFormDefaults(),
      ...vRole,
    };
    return new FormGroup<VRoleFormGroupContent>({
      id: new FormControl(
        { value: vRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(vRoleRawValue.code),
      name: new FormControl(vRoleRawValue.name),
      description: new FormControl(vRoleRawValue.description),
      applicationId: new FormControl(vRoleRawValue.applicationId),
      vUsers: new FormControl(vRoleRawValue.vUsers ?? []),
      vOrganizations: new FormControl(vRoleRawValue.vOrganizations ?? []),
    });
  }

  getVRole(form: VRoleFormGroup): IVRole | NewVRole {
    return form.getRawValue() as IVRole | NewVRole;
  }

  resetForm(form: VRoleFormGroup, vRole: VRoleFormGroupInput): void {
    const vRoleRawValue = { ...this.getFormDefaults(), ...vRole };
    form.reset(
      {
        ...vRoleRawValue,
        id: { value: vRoleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VRoleFormDefaults {
    return {
      id: null,
      vUsers: [],
      vOrganizations: [],
    };
  }
}
