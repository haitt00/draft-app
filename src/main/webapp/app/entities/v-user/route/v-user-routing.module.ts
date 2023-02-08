import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VUserComponent } from '../list/v-user.component';
import { VUserDetailComponent } from '../detail/v-user-detail.component';
import { VUserUpdateComponent } from '../update/v-user-update.component';
import { VUserRoutingResolveService } from './v-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vUserRoute: Routes = [
  {
    path: '',
    component: VUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VUserDetailComponent,
    resolve: {
      vUser: VUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VUserUpdateComponent,
    resolve: {
      vUser: VUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VUserUpdateComponent,
    resolve: {
      vUser: VUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vUserRoute)],
  exports: [RouterModule],
})
export class VUserRoutingModule {}
