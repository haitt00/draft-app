import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VRoleFormService, VRoleFormGroup } from './v-role-form.service';
import { IVRole } from '../v-role.model';
import { VRoleService } from '../service/v-role.service';

@Component({
  selector: 'jhi-v-role-update',
  templateUrl: './v-role-update.component.html',
})
export class VRoleUpdateComponent implements OnInit {
  isSaving = false;
  vRole: IVRole | null = null;

  editForm: VRoleFormGroup = this.vRoleFormService.createVRoleFormGroup();

  constructor(
    protected vRoleService: VRoleService,
    protected vRoleFormService: VRoleFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vRole }) => {
      this.vRole = vRole;
      if (vRole) {
        this.updateForm(vRole);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vRole = this.vRoleFormService.getVRole(this.editForm);
    if (vRole.id !== null) {
      this.subscribeToSaveResponse(this.vRoleService.update(vRole));
    } else {
      this.subscribeToSaveResponse(this.vRoleService.create(vRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVRole>>): void {
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

  protected updateForm(vRole: IVRole): void {
    this.vRole = vRole;
    this.vRoleFormService.resetForm(this.editForm, vRole);
  }
}
