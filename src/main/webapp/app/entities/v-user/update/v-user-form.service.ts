import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVUser, NewVUser } from '../v-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVUser for edit and NewVUserFormGroupInput for create.
 */
type VUserFormGroupInput = IVUser | PartialWithRequiredKeyOf<NewVUser>;

type VUserFormDefaults = Pick<NewVUser, 'id' | 'enabled' | 'vRoles' | 'vOrganizations'>;

type VUserFormGroupContent = {
  id: FormControl<IVUser['id'] | NewVUser['id']>;
  username: FormControl<IVUser['username']>;
  fullname: FormControl<IVUser['fullname']>;
  firstname: FormControl<IVUser['firstname']>;
  lastname: FormControl<IVUser['lastname']>;
  password: FormControl<IVUser['password']>;
  email: FormControl<IVUser['email']>;
  emailVerified: FormControl<IVUser['emailVerified']>;
  language: FormControl<IVUser['language']>;
  enabled: FormControl<IVUser['enabled']>;
  vRoles: FormControl<IVUser['vRoles']>;
  vOrganizations: FormControl<IVUser['vOrganizations']>;
};

export type VUserFormGroup = FormGroup<VUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VUserFormService {
  createVUserFormGroup(vUser: VUserFormGroupInput = { id: null }): VUserFormGroup {
    const vUserRawValue = {
      ...this.getFormDefaults(),
      ...vUser,
    };
    return new FormGroup<VUserFormGroupContent>({
      id: new FormControl(
        { value: vUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      username: new FormControl(vUserRawValue.username),
      fullname: new FormControl(vUserRawValue.fullname),
      firstname: new FormControl(vUserRawValue.firstname),
      lastname: new FormControl(vUserRawValue.lastname),
      password: new FormControl(vUserRawValue.password),
      email: new FormControl(vUserRawValue.email),
      emailVerified: new FormControl(vUserRawValue.emailVerified),
      language: new FormControl(vUserRawValue.language),
      enabled: new FormControl(vUserRawValue.enabled),
      vRoles: new FormControl(vUserRawValue.vRoles ?? []),
      vOrganizations: new FormControl(vUserRawValue.vOrganizations ?? []),
    });
  }

  getVUser(form: VUserFormGroup): IVUser | NewVUser {
    return form.getRawValue() as IVUser | NewVUser;
  }

  resetForm(form: VUserFormGroup, vUser: VUserFormGroupInput): void {
    const vUserRawValue = { ...this.getFormDefaults(), ...vUser };
    form.reset(
      {
        ...vUserRawValue,
        id: { value: vUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VUserFormDefaults {
    return {
      id: null,
      enabled: false,
      vRoles: [],
      vOrganizations: [],
    };
  }
}
