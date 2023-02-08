import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VRoleComponent } from './list/v-role.component';
import { VRoleDetailComponent } from './detail/v-role-detail.component';
import { VRoleUpdateComponent } from './update/v-role-update.component';
import { VRoleDeleteDialogComponent } from './delete/v-role-delete-dialog.component';
import { VRoleRoutingModule } from './route/v-role-routing.module';

@NgModule({
  imports: [SharedModule, VRoleRoutingModule],
  declarations: [VRoleComponent, VRoleDetailComponent, VRoleUpdateComponent, VRoleDeleteDialogComponent],
})
export class VRoleModule {}
