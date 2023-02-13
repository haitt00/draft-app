import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VPermissionComponent } from './list/v-permission.component';
import { VPermissionDetailComponent } from './detail/v-permission-detail.component';
import { VPermissionUpdateComponent } from './update/v-permission-update.component';
import { VPermissionDeleteDialogComponent } from './delete/v-permission-delete-dialog.component';
import { VPermissionRoutingModule } from './route/v-permission-routing.module';

@NgModule({
  imports: [SharedModule, VPermissionRoutingModule],
  declarations: [VPermissionComponent, VPermissionDetailComponent, VPermissionUpdateComponent, VPermissionDeleteDialogComponent],
})
export class VPermissionModule {}
