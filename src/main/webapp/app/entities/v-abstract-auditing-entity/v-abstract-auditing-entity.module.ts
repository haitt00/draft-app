import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VAbstractAuditingEntityComponent } from './list/v-abstract-auditing-entity.component';
import { VAbstractAuditingEntityDetailComponent } from './detail/v-abstract-auditing-entity-detail.component';
import { VAbstractAuditingEntityUpdateComponent } from './update/v-abstract-auditing-entity-update.component';
import { VAbstractAuditingEntityDeleteDialogComponent } from './delete/v-abstract-auditing-entity-delete-dialog.component';
import { VAbstractAuditingEntityRoutingModule } from './route/v-abstract-auditing-entity-routing.module';

@NgModule({
  imports: [SharedModule, VAbstractAuditingEntityRoutingModule],
  declarations: [
    VAbstractAuditingEntityComponent,
    VAbstractAuditingEntityDetailComponent,
    VAbstractAuditingEntityUpdateComponent,
    VAbstractAuditingEntityDeleteDialogComponent,
  ],
})
export class VAbstractAuditingEntityModule {}
