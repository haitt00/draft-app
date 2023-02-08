import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVRole, NewVRole } from '../v-role.model';

export type PartialUpdateVRole = Partial<IVRole> & Pick<IVRole, 'id'>;

export type EntityResponseType = HttpResponse<IVRole>;
export type EntityArrayResponseType = HttpResponse<IVRole[]>;

@Injectable({ providedIn: 'root' })
export class VRoleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-roles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vRole: NewVRole): Observable<EntityResponseType> {
    return this.http.post<IVRole>(this.resourceUrl, vRole, { observe: 'response' });
  }

  update(vRole: IVRole): Observable<EntityResponseType> {
    return this.http.put<IVRole>(`${this.resourceUrl}/${this.getVRoleIdentifier(vRole)}`, vRole, { observe: 'response' });
  }

  partialUpdate(vRole: PartialUpdateVRole): Observable<EntityResponseType> {
    return this.http.patch<IVRole>(`${this.resourceUrl}/${this.getVRoleIdentifier(vRole)}`, vRole, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVRoleIdentifier(vRole: Pick<IVRole, 'id'>): number {
    return vRole.id;
  }

  compareVRole(o1: Pick<IVRole, 'id'> | null, o2: Pick<IVRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getVRoleIdentifier(o1) === this.getVRoleIdentifier(o2) : o1 === o2;
  }

  addVRoleToCollectionIfMissing<Type extends Pick<IVRole, 'id'>>(
    vRoleCollection: Type[],
    ...vRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vRoles: Type[] = vRolesToCheck.filter(isPresent);
    if (vRoles.length > 0) {
      const vRoleCollectionIdentifiers = vRoleCollection.map(vRoleItem => this.getVRoleIdentifier(vRoleItem)!);
      const vRolesToAdd = vRoles.filter(vRoleItem => {
        const vRoleIdentifier = this.getVRoleIdentifier(vRoleItem);
        if (vRoleCollectionIdentifiers.includes(vRoleIdentifier)) {
          return false;
        }
        vRoleCollectionIdentifiers.push(vRoleIdentifier);
        return true;
      });
      return [...vRolesToAdd, ...vRoleCollection];
    }
    return vRoleCollection;
  }
}
