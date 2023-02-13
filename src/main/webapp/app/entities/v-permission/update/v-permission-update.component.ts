import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VPermissionFormService, VPermissionFormGroup } from './v-permission-form.service';
import { IVPermission } from '../v-permission.model';
import { VPermissionService } from '../service/v-permission.service';

@Component({
  selector: 'jhi-v-permission-update',
  templateUrl: './v-permission-update.component.html',
})
export class VPermissionUpdateComponent implements OnInit {
  isSaving = false;
  vPermission: IVPermission | null = null;

  editForm: VPermissionFormGroup = this.vPermissionFormService.createVPermissionFormGroup();

  constructor(
    protected vPermissionService: VPermissionService,
    protected vPermissionFormService: VPermissionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vPermission }) => {
      this.vPermission = vPermission;
      if (vPermission) {
        this.updateForm(vPermission);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vPermission = this.vPermissionFormService.getVPermission(this.editForm);
    if (vPermission.id !== null) {
      this.subscribeToSaveResponse(this.vPermissionService.update(vPermission));
    } else {
      this.subscribeToSaveResponse(this.vPermissionService.create(vPermission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVPermission>>): void {
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

  protected updateForm(vPermission: IVPermission): void {
    this.vPermission = vPermission;
    this.vPermissionFormService.resetForm(this.editForm, vPermission);
  }
}
