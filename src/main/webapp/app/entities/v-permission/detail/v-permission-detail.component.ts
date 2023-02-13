import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVPermission } from '../v-permission.model';

@Component({
  selector: 'jhi-v-permission-detail',
  templateUrl: './v-permission-detail.component.html',
})
export class VPermissionDetailComponent implements OnInit {
  vPermission: IVPermission | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vPermission }) => {
      this.vPermission = vPermission;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
