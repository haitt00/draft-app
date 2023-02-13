import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVPermission } from '../v-permission.model';
import { VPermissionService } from '../service/v-permission.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './v-permission-delete-dialog.component.html',
})
export class VPermissionDeleteDialogComponent {
  vPermission?: IVPermission;

  constructor(protected vPermissionService: VPermissionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vPermissionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
