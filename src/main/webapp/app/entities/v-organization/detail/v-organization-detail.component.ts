import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVOrganization } from '../v-organization.model';

@Component({
  selector: 'jhi-v-organization-detail',
  templateUrl: './v-organization-detail.component.html',
})
export class VOrganizationDetailComponent implements OnInit {
  vOrganization: IVOrganization | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vOrganization }) => {
      this.vOrganization = vOrganization;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
