import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVTenant } from '../v-tenant.model';
import { VTenantService } from '../service/v-tenant.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './v-tenant-delete-dialog.component.html',
})
export class VTenantDeleteDialogComponent {
  vTenant?: IVTenant;

  constructor(protected vTenantService: VTenantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vTenantService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
