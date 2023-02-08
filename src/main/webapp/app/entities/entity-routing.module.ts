import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'v-abstract-auditing-entity',
        data: { pageTitle: 'draftApp.vAbstractAuditingEntity.home.title' },
        loadChildren: () =>
          import('./v-abstract-auditing-entity/v-abstract-auditing-entity.module').then(m => m.VAbstractAuditingEntityModule),
      },
      {
        path: 'v-tenant',
        data: { pageTitle: 'draftApp.vTenant.home.title' },
        loadChildren: () => import('./v-tenant/v-tenant.module').then(m => m.VTenantModule),
      },
      {
        path: 'v-organization',
        data: { pageTitle: 'draftApp.vOrganization.home.title' },
        loadChildren: () => import('./v-organization/v-organization.module').then(m => m.VOrganizationModule),
      },
      {
        path: 'v-user',
        data: { pageTitle: 'draftApp.vUser.home.title' },
        loadChildren: () => import('./v-user/v-user.module').then(m => m.VUserModule),
      },
      {
        path: 'v-role',
        data: { pageTitle: 'draftApp.vRole.home.title' },
        loadChildren: () => import('./v-role/v-role.module').then(m => m.VRoleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
