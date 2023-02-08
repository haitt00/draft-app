import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VTenantComponent } from './list/v-tenant.component';
import { VTenantDetailComponent } from './detail/v-tenant-detail.component';
import { VTenantUpdateComponent } from './update/v-tenant-update.component';
import { VTenantDeleteDialogComponent } from './delete/v-tenant-delete-dialog.component';
import { VTenantRoutingModule } from './route/v-tenant-routing.module';

@NgModule({
  imports: [SharedModule, VTenantRoutingModule],
  declarations: [VTenantComponent, VTenantDetailComponent, VTenantUpdateComponent, VTenantDeleteDialogComponent],
})
export class VTenantModule {}
