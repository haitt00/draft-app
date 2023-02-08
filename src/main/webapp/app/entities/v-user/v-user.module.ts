import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VUserComponent } from './list/v-user.component';
import { VUserDetailComponent } from './detail/v-user-detail.component';
import { VUserUpdateComponent } from './update/v-user-update.component';
import { VUserDeleteDialogComponent } from './delete/v-user-delete-dialog.component';
import { VUserRoutingModule } from './route/v-user-routing.module';

@NgModule({
  imports: [SharedModule, VUserRoutingModule],
  declarations: [VUserComponent, VUserDetailComponent, VUserUpdateComponent, VUserDeleteDialogComponent],
})
export class VUserModule {}
