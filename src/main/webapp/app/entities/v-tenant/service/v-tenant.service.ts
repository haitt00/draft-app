import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVTenant, NewVTenant } from '../v-tenant.model';

export type PartialUpdateVTenant = Partial<IVTenant> & Pick<IVTenant, 'id'>;

export type EntityResponseType = HttpResponse<IVTenant>;
export type EntityArrayResponseType = HttpResponse<IVTenant[]>;

@Injectable({ providedIn: 'root' })
export class VTenantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-tenants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vTenant: NewVTenant): Observable<EntityResponseType> {
    return this.http.post<IVTenant>(this.resourceUrl, vTenant, { observe: 'response' });
  }

  update(vTenant: IVTenant): Observable<EntityResponseType> {
    return this.http.put<IVTenant>(`${this.resourceUrl}/${this.getVTenantIdentifier(vTenant)}`, vTenant, { observe: 'response' });
  }

  partialUpdate(vTenant: PartialUpdateVTenant): Observable<EntityResponseType> {
    return this.http.patch<IVTenant>(`${this.resourceUrl}/${this.getVTenantIdentifier(vTenant)}`, vTenant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVTenant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVTenant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVTenantIdentifier(vTenant: Pick<IVTenant, 'id'>): number {
    return vTenant.id;
  }

  compareVTenant(o1: Pick<IVTenant, 'id'> | null, o2: Pick<IVTenant, 'id'> | null): boolean {
    return o1 && o2 ? this.getVTenantIdentifier(o1) === this.getVTenantIdentifier(o2) : o1 === o2;
  }

  addVTenantToCollectionIfMissing<Type extends Pick<IVTenant, 'id'>>(
    vTenantCollection: Type[],
    ...vTenantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vTenants: Type[] = vTenantsToCheck.filter(isPresent);
    if (vTenants.length > 0) {
      const vTenantCollectionIdentifiers = vTenantCollection.map(vTenantItem => this.getVTenantIdentifier(vTenantItem)!);
      const vTenantsToAdd = vTenants.filter(vTenantItem => {
        const vTenantIdentifier = this.getVTenantIdentifier(vTenantItem);
        if (vTenantCollectionIdentifiers.includes(vTenantIdentifier)) {
          return false;
        }
        vTenantCollectionIdentifiers.push(vTenantIdentifier);
        return true;
      });
      return [...vTenantsToAdd, ...vTenantCollection];
    }
    return vTenantCollection;
  }
}
