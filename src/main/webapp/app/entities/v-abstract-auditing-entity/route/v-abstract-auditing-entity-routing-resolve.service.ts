import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';
import { VAbstractAuditingEntityService } from '../service/v-abstract-auditing-entity.service';

@Injectable({ providedIn: 'root' })
export class VAbstractAuditingEntityRoutingResolveService implements Resolve<IVAbstractAuditingEntity | null> {
  constructor(protected service: VAbstractAuditingEntityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVAbstractAuditingEntity | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vAbstractAuditingEntity: HttpResponse<IVAbstractAuditingEntity>) => {
          if (vAbstractAuditingEntity.body) {
            return of(vAbstractAuditingEntity.body);
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
