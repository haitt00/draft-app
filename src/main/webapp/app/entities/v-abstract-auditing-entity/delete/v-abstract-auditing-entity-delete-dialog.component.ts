import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';
import { VAbstractAuditingEntityService } from '../service/v-abstract-auditing-entity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './v-abstract-auditing-entity-delete-dialog.component.html',
})
export class VAbstractAuditingEntityDeleteDialogComponent {
  vAbstractAuditingEntity?: IVAbstractAuditingEntity;

  constructor(protected vAbstractAuditingEntityService: VAbstractAuditingEntityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vAbstractAuditingEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
