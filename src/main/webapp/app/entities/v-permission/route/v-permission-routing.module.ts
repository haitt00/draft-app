import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VPermissionComponent } from '../list/v-permission.component';
import { VPermissionDetailComponent } from '../detail/v-permission-detail.component';
import { VPermissionUpdateComponent } from '../update/v-permission-update.component';
import { VPermissionRoutingResolveService } from './v-permission-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vPermissionRoute: Routes = [
  {
    path: '',
    component: VPermissionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VPermissionDetailComponent,
    resolve: {
      vPermission: VPermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VPermissionUpdateComponent,
    resolve: {
      vPermission: VPermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VPermissionUpdateComponent,
    resolve: {
      vPermission: VPermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vPermissionRoute)],
  exports: [RouterModule],
})
export class VPermissionRoutingModule {}
