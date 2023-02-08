import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVUser } from '../v-user.model';

@Component({
  selector: 'jhi-v-user-detail',
  templateUrl: './v-user-detail.component.html',
})
export class VUserDetailComponent implements OnInit {
  vUser: IVUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vUser }) => {
      this.vUser = vUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
