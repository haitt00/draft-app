import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VOrganizationFormService, VOrganizationFormGroup } from './v-organization-form.service';
import { IVOrganization } from '../v-organization.model';
import { VOrganizationService } from '../service/v-organization.service';
import { IVUser } from 'app/entities/v-user/v-user.model';
import { VUserService } from 'app/entities/v-user/service/v-user.service';
import { IVRole } from 'app/entities/v-role/v-role.model';
import { VRoleService } from 'app/entities/v-role/service/v-role.service';

@Component({
  selector: 'jhi-v-organization-update',
  templateUrl: './v-organization-update.component.html',
})
export class VOrganizationUpdateComponent implements OnInit {
  isSaving = false;
  vOrganization: IVOrganization | null = null;

  vUsersSharedCollection: IVUser[] = [];
  vRolesSharedCollection: IVRole[] = [];

  editForm: VOrganizationFormGroup = this.vOrganizationFormService.createVOrganizationFormGroup();

  constructor(
    protected vOrganizationService: VOrganizationService,
    protected vOrganizationFormService: VOrganizationFormService,
    protected vUserService: VUserService,
    protected vRoleService: VRoleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVUser = (o1: IVUser | null, o2: IVUser | null): boolean => this.vUserService.compareVUser(o1, o2);

  compareVRole = (o1: IVRole | null, o2: IVRole | null): boolean => this.vRoleService.compareVRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vOrganization }) => {
      this.vOrganization = vOrganization;
      if (vOrganization) {
        this.updateForm(vOrganization);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vOrganization = this.vOrganizationFormService.getVOrganization(this.editForm);
    if (vOrganization.id !== null) {
      this.subscribeToSaveResponse(this.vOrganizationService.update(vOrganization));
    } else {
      this.subscribeToSaveResponse(this.vOrganizationService.create(vOrganization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVOrganization>>): void {
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

  protected updateForm(vOrganization: IVOrganization): void {
    this.vOrganization = vOrganization;
    this.vOrganizationFormService.resetForm(this.editForm, vOrganization);

    this.vUsersSharedCollection = this.vUserService.addVUserToCollectionIfMissing<IVUser>(
      this.vUsersSharedCollection,
      ...(vOrganization.vUsers ?? [])
    );
    this.vRolesSharedCollection = this.vRoleService.addVRoleToCollectionIfMissing<IVRole>(
      this.vRolesSharedCollection,
      ...(vOrganization.vRoles ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vUserService
      .query()
      .pipe(map((res: HttpResponse<IVUser[]>) => res.body ?? []))
      .pipe(
        map((vUsers: IVUser[]) => this.vUserService.addVUserToCollectionIfMissing<IVUser>(vUsers, ...(this.vOrganization?.vUsers ?? [])))
      )
      .subscribe((vUsers: IVUser[]) => (this.vUsersSharedCollection = vUsers));

    this.vRoleService
      .query()
      .pipe(map((res: HttpResponse<IVRole[]>) => res.body ?? []))
      .pipe(
        map((vRoles: IVRole[]) => this.vRoleService.addVRoleToCollectionIfMissing<IVRole>(vRoles, ...(this.vOrganization?.vRoles ?? [])))
      )
      .subscribe((vRoles: IVRole[]) => (this.vRolesSharedCollection = vRoles));
  }
}
