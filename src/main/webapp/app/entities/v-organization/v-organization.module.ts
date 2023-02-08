import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VOrganizationComponent } from './list/v-organization.component';
import { VOrganizationDetailComponent } from './detail/v-organization-detail.component';
import { VOrganizationUpdateComponent } from './update/v-organization-update.component';
import { VOrganizationDeleteDialogComponent } from './delete/v-organization-delete-dialog.component';
import { VOrganizationRoutingModule } from './route/v-organization-routing.module';

@NgModule({
  imports: [SharedModule, VOrganizationRoutingModule],
  declarations: [VOrganizationComponent, VOrganizationDetailComponent, VOrganizationUpdateComponent, VOrganizationDeleteDialogComponent],
})
export class VOrganizationModule {}
