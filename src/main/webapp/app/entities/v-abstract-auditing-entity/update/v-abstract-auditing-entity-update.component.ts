import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VAbstractAuditingEntityFormService, VAbstractAuditingEntityFormGroup } from './v-abstract-auditing-entity-form.service';
import { IVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';
import { VAbstractAuditingEntityService } from '../service/v-abstract-auditing-entity.service';

@Component({
  selector: 'jhi-v-abstract-auditing-entity-update',
  templateUrl: './v-abstract-auditing-entity-update.component.html',
})
export class VAbstractAuditingEntityUpdateComponent implements OnInit {
  isSaving = false;
  vAbstractAuditingEntity: IVAbstractAuditingEntity | null = null;

  editForm: VAbstractAuditingEntityFormGroup = this.vAbstractAuditingEntityFormService.createVAbstractAuditingEntityFormGroup();

  constructor(
    protected vAbstractAuditingEntityService: VAbstractAuditingEntityService,
    protected vAbstractAuditingEntityFormService: VAbstractAuditingEntityFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vAbstractAuditingEntity }) => {
      this.vAbstractAuditingEntity = vAbstractAuditingEntity;
      if (vAbstractAuditingEntity) {
        this.updateForm(vAbstractAuditingEntity);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vAbstractAuditingEntity = this.vAbstractAuditingEntityFormService.getVAbstractAuditingEntity(this.editForm);
    if (vAbstractAuditingEntity.id !== null) {
      this.subscribeToSaveResponse(this.vAbstractAuditingEntityService.update(vAbstractAuditingEntity));
    } else {
      this.subscribeToSaveResponse(this.vAbstractAuditingEntityService.create(vAbstractAuditingEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVAbstractAuditingEntity>>): void {
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

  protected updateForm(vAbstractAuditingEntity: IVAbstractAuditingEntity): void {
    this.vAbstractAuditingEntity = vAbstractAuditingEntity;
    this.vAbstractAuditingEntityFormService.resetForm(this.editForm, vAbstractAuditingEntity);
  }
}
