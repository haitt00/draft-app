import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVTenant } from '../v-tenant.model';

@Component({
  selector: 'jhi-v-tenant-detail',
  templateUrl: './v-tenant-detail.component.html',
})
export class VTenantDetailComponent implements OnInit {
  vTenant: IVTenant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vTenant }) => {
      this.vTenant = vTenant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
