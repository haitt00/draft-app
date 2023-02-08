import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVRole } from '../v-role.model';
import { VRoleService } from '../service/v-role.service';

@Injectable({ providedIn: 'root' })
export class VRoleRoutingResolveService implements Resolve<IVRole | null> {
  constructor(protected service: VRoleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVRole | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vRole: HttpResponse<IVRole>) => {
          if (vRole.body) {
            return of(vRole.body);
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
