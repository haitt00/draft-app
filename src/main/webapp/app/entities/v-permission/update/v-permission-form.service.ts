import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVPermission, NewVPermission } from '../v-permission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVPermission for edit and NewVPermissionFormGroupInput for create.
 */
type VPermissionFormGroupInput = IVPermission | PartialWithRequiredKeyOf<NewVPermission>;

type VPermissionFormDefaults = Pick<NewVPermission, 'id'>;

type VPermissionFormGroupContent = {
  id: FormControl<IVPermission['id'] | NewVPermission['id']>;
  parentId: FormControl<IVPermission['parentId']>;
  code: FormControl<IVPermission['code']>;
  name: FormControl<IVPermission['name']>;
  description: FormControl<IVPermission['description']>;
  type: FormControl<IVPermission['type']>;
  order: FormControl<IVPermission['order']>;
  url: FormControl<IVPermission['url']>;
  component: FormControl<IVPermission['component']>;
  perms: FormControl<IVPermission['perms']>;
};

export type VPermissionFormGroup = FormGroup<VPermissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VPermissionFormService {
  createVPermissionFormGroup(vPermission: VPermissionFormGroupInput = { id: null }): VPermissionFormGroup {
    const vPermissionRawValue = {
      ...this.getFormDefaults(),
      ...vPermission,
    };
    return new FormGroup<VPermissionFormGroupContent>({
      id: new FormControl(
        { value: vPermissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      parentId: new FormControl(vPermissionRawValue.parentId),
      code: new FormControl(vPermissionRawValue.code),
      name: new FormControl(vPermissionRawValue.name),
      description: new FormControl(vPermissionRawValue.description),
      type: new FormControl(vPermissionRawValue.type),
      order: new FormControl(vPermissionRawValue.order),
      url: new FormControl(vPermissionRawValue.url),
      component: new FormControl(vPermissionRawValue.component),
      perms: new FormControl(vPermissionRawValue.perms),
    });
  }

  getVPermission(form: VPermissionFormGroup): IVPermission | NewVPermission {
    return form.getRawValue() as IVPermission | NewVPermission;
  }

  resetForm(form: VPermissionFormGroup, vPermission: VPermissionFormGroupInput): void {
    const vPermissionRawValue = { ...this.getFormDefaults(), ...vPermission };
    form.reset(
      {
        ...vPermissionRawValue,
        id: { value: vPermissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VPermissionFormDefaults {
    return {
      id: null,
    };
  }
}
