import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'v-permission',
        data: { pageTitle: 'draftApp.vPermission.home.title' },
        loadChildren: () => import('./v-permission/v-permission.module').then(m => m.VPermissionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
