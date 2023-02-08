import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VTenantComponent } from '../list/v-tenant.component';
import { VTenantDetailComponent } from '../detail/v-tenant-detail.component';
import { VTenantUpdateComponent } from '../update/v-tenant-update.component';
import { VTenantRoutingResolveService } from './v-tenant-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vTenantRoute: Routes = [
  {
    path: '',
    component: VTenantComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VTenantDetailComponent,
    resolve: {
      vTenant: VTenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VTenantUpdateComponent,
    resolve: {
      vTenant: VTenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VTenantUpdateComponent,
    resolve: {
      vTenant: VTenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vTenantRoute)],
  exports: [RouterModule],
})
export class VTenantRoutingModule {}
