import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVRole } from '../v-role.model';

@Component({
  selector: 'jhi-v-role-detail',
  templateUrl: './v-role-detail.component.html',
})
export class VRoleDetailComponent implements OnInit {
  vRole: IVRole | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vRole }) => {
      this.vRole = vRole;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
