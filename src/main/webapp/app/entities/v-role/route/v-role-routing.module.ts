import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VRoleComponent } from '../list/v-role.component';
import { VRoleDetailComponent } from '../detail/v-role-detail.component';
import { VRoleUpdateComponent } from '../update/v-role-update.component';
import { VRoleRoutingResolveService } from './v-role-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vRoleRoute: Routes = [
  {
    path: '',
    component: VRoleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VRoleDetailComponent,
    resolve: {
      vRole: VRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VRoleUpdateComponent,
    resolve: {
      vRole: VRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VRoleUpdateComponent,
    resolve: {
      vRole: VRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vRoleRoute)],
  exports: [RouterModule],
})
export class VRoleRoutingModule {}
