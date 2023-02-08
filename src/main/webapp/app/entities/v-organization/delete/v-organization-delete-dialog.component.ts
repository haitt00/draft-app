import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVOrganization } from '../v-organization.model';
import { VOrganizationService } from '../service/v-organization.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './v-organization-delete-dialog.component.html',
})
export class VOrganizationDeleteDialogComponent {
  vOrganization?: IVOrganization;

  constructor(protected vOrganizationService: VOrganizationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vOrganizationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
