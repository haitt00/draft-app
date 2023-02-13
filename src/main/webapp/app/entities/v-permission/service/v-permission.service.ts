import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVPermission, NewVPermission } from '../v-permission.model';

export type PartialUpdateVPermission = Partial<IVPermission> & Pick<IVPermission, 'id'>;

export type EntityResponseType = HttpResponse<IVPermission>;
export type EntityArrayResponseType = HttpResponse<IVPermission[]>;

@Injectable({ providedIn: 'root' })
export class VPermissionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-permissions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vPermission: NewVPermission): Observable<EntityResponseType> {
    return this.http.post<IVPermission>(this.resourceUrl, vPermission, { observe: 'response' });
  }

  update(vPermission: IVPermission): Observable<EntityResponseType> {
    return this.http.put<IVPermission>(`${this.resourceUrl}/${this.getVPermissionIdentifier(vPermission)}`, vPermission, {
      observe: 'response',
    });
  }

  partialUpdate(vPermission: PartialUpdateVPermission): Observable<EntityResponseType> {
    return this.http.patch<IVPermission>(`${this.resourceUrl}/${this.getVPermissionIdentifier(vPermission)}`, vPermission, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVPermission[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVPermissionIdentifier(vPermission: Pick<IVPermission, 'id'>): number {
    return vPermission.id;
  }

  compareVPermission(o1: Pick<IVPermission, 'id'> | null, o2: Pick<IVPermission, 'id'> | null): boolean {
    return o1 && o2 ? this.getVPermissionIdentifier(o1) === this.getVPermissionIdentifier(o2) : o1 === o2;
  }

  addVPermissionToCollectionIfMissing<Type extends Pick<IVPermission, 'id'>>(
    vPermissionCollection: Type[],
    ...vPermissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vPermissions: Type[] = vPermissionsToCheck.filter(isPresent);
    if (vPermissions.length > 0) {
      const vPermissionCollectionIdentifiers = vPermissionCollection.map(
        vPermissionItem => this.getVPermissionIdentifier(vPermissionItem)!
      );
      const vPermissionsToAdd = vPermissions.filter(vPermissionItem => {
        const vPermissionIdentifier = this.getVPermissionIdentifier(vPermissionItem);
        if (vPermissionCollectionIdentifiers.includes(vPermissionIdentifier)) {
          return false;
        }
        vPermissionCollectionIdentifiers.push(vPermissionIdentifier);
        return true;
      });
      return [...vPermissionsToAdd, ...vPermissionCollection];
    }
    return vPermissionCollection;
  }
}
