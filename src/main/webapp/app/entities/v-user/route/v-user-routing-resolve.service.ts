import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVUser } from '../v-user.model';
import { VUserService } from '../service/v-user.service';

@Injectable({ providedIn: 'root' })
export class VUserRoutingResolveService implements Resolve<IVUser | null> {
  constructor(protected service: VUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vUser: HttpResponse<IVUser>) => {
          if (vUser.body) {
            return of(vUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
