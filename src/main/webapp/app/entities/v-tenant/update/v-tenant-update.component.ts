import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VTenantFormService, VTenantFormGroup } from './v-tenant-form.service';
import { IVTenant } from '../v-tenant.model';
import { VTenantService } from '../service/v-tenant.service';

@Component({
  selector: 'jhi-v-tenant-update',
  templateUrl: './v-tenant-update.component.html',
})
export class VTenantUpdateComponent implements OnInit {
  isSaving = false;
  vTenant: IVTenant | null = null;

  editForm: VTenantFormGroup = this.vTenantFormService.createVTenantFormGroup();

  constructor(
    protected vTenantService: VTenantService,
    protected vTenantFormService: VTenantFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vTenant }) => {
      this.vTenant = vTenant;
      if (vTenant) {
        this.updateForm(vTenant);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vTenant = this.vTenantFormService.getVTenant(this.editForm);
    if (vTenant.id !== null) {
      this.subscribeToSaveResponse(this.vTenantService.update(vTenant));
    } else {
      this.subscribeToSaveResponse(this.vTenantService.create(vTenant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVTenant>>): void {
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

  protected updateForm(vTenant: IVTenant): void {
    this.vTenant = vTenant;
    this.vTenantFormService.resetForm(this.editForm, vTenant);
  }
}
