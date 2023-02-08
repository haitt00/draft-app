import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VAbstractAuditingEntityComponent } from '../list/v-abstract-auditing-entity.component';
import { VAbstractAuditingEntityDetailComponent } from '../detail/v-abstract-auditing-entity-detail.component';
import { VAbstractAuditingEntityUpdateComponent } from '../update/v-abstract-auditing-entity-update.component';
import { VAbstractAuditingEntityRoutingResolveService } from './v-abstract-auditing-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vAbstractAuditingEntityRoute: Routes = [
  {
    path: '',
    component: VAbstractAuditingEntityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VAbstractAuditingEntityDetailComponent,
    resolve: {
      vAbstractAuditingEntity: VAbstractAuditingEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VAbstractAuditingEntityUpdateComponent,
    resolve: {
      vAbstractAuditingEntity: VAbstractAuditingEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VAbstractAuditingEntityUpdateComponent,
    resolve: {
      vAbstractAuditingEntity: VAbstractAuditingEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vAbstractAuditingEntityRoute)],
  exports: [RouterModule],
})
export class VAbstractAuditingEntityRoutingModule {}
