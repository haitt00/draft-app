import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVPermission } from '../v-permission.model';
import { VPermissionService } from '../service/v-permission.service';

@Injectable({ providedIn: 'root' })
export class VPermissionRoutingResolveService implements Resolve<IVPermission | null> {
  constructor(protected service: VPermissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVPermission | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vPermission: HttpResponse<IVPermission>) => {
          if (vPermission.body) {
            return of(vPermission.body);
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
