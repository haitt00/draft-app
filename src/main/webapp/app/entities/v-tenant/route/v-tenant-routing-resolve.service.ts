import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVTenant } from '../v-tenant.model';
import { VTenantService } from '../service/v-tenant.service';

@Injectable({ providedIn: 'root' })
export class VTenantRoutingResolveService implements Resolve<IVTenant | null> {
  constructor(protected service: VTenantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVTenant | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vTenant: HttpResponse<IVTenant>) => {
          if (vTenant.body) {
            return of(vTenant.body);
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
