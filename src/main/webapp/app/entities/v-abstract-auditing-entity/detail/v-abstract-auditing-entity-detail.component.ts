import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';

@Component({
  selector: 'jhi-v-abstract-auditing-entity-detail',
  templateUrl: './v-abstract-auditing-entity-detail.component.html',
})
export class VAbstractAuditingEntityDetailComponent implements OnInit {
  vAbstractAuditingEntity: IVAbstractAuditingEntity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vAbstractAuditingEntity }) => {
      this.vAbstractAuditingEntity = vAbstractAuditingEntity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
