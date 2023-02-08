import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VOrganizationComponent } from '../list/v-organization.component';
import { VOrganizationDetailComponent } from '../detail/v-organization-detail.component';
import { VOrganizationUpdateComponent } from '../update/v-organization-update.component';
import { VOrganizationRoutingResolveService } from './v-organization-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vOrganizationRoute: Routes = [
  {
    path: '',
    component: VOrganizationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VOrganizationDetailComponent,
    resolve: {
      vOrganization: VOrganizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VOrganizationUpdateComponent,
    resolve: {
      vOrganization: VOrganizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VOrganizationUpdateComponent,
    resolve: {
      vOrganization: VOrganizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vOrganizationRoute)],
  exports: [RouterModule],
})
export class VOrganizationRoutingModule {}
