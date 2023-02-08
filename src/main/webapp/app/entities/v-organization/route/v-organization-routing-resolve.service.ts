import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVOrganization } from '../v-organization.model';
import { VOrganizationService } from '../service/v-organization.service';

@Injectable({ providedIn: 'root' })
export class VOrganizationRoutingResolveService implements Resolve<IVOrganization | null> {
  constructor(protected service: VOrganizationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVOrganization | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vOrganization: HttpResponse<IVOrganization>) => {
          if (vOrganization.body) {
            return of(vOrganization.body);
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
