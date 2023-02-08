import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VUserFormService, VUserFormGroup } from './v-user-form.service';
import { IVUser } from '../v-user.model';
import { VUserService } from '../service/v-user.service';
import { IVRole } from 'app/entities/v-role/v-role.model';
import { VRoleService } from 'app/entities/v-role/service/v-role.service';

@Component({
  selector: 'jhi-v-user-update',
  templateUrl: './v-user-update.component.html',
})
export class VUserUpdateComponent implements OnInit {
  isSaving = false;
  vUser: IVUser | null = null;

  vRolesSharedCollection: IVRole[] = [];

  editForm: VUserFormGroup = this.vUserFormService.createVUserFormGroup();

  constructor(
    protected vUserService: VUserService,
    protected vUserFormService: VUserFormService,
    protected vRoleService: VRoleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVRole = (o1: IVRole | null, o2: IVRole | null): boolean => this.vRoleService.compareVRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vUser }) => {
      this.vUser = vUser;
      if (vUser) {
        this.updateForm(vUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vUser = this.vUserFormService.getVUser(this.editForm);
    if (vUser.id !== null) {
      this.subscribeToSaveResponse(this.vUserService.update(vUser));
    } else {
      this.subscribeToSaveResponse(this.vUserService.create(vUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vUser: IVUser): void {
    this.vUser = vUser;
    this.vUserFormService.resetForm(this.editForm, vUser);

    this.vRolesSharedCollection = this.vRoleService.addVRoleToCollectionIfMissing<IVRole>(
      this.vRolesSharedCollection,
      ...(vUser.vRoles ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vRoleService
      .query()
      .pipe(map((res: HttpResponse<IVRole[]>) => res.body ?? []))
      .pipe(map((vRoles: IVRole[]) => this.vRoleService.addVRoleToCollectionIfMissing<IVRole>(vRoles, ...(this.vUser?.vRoles ?? []))))
      .subscribe((vRoles: IVRole[]) => (this.vRolesSharedCollection = vRoles));
  }
}
