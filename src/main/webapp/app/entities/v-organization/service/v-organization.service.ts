import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVOrganization, NewVOrganization } from '../v-organization.model';

export type PartialUpdateVOrganization = Partial<IVOrganization> & Pick<IVOrganization, 'id'>;

export type EntityResponseType = HttpResponse<IVOrganization>;
export type EntityArrayResponseType = HttpResponse<IVOrganization[]>;

@Injectable({ providedIn: 'root' })
export class VOrganizationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-organizations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vOrganization: NewVOrganization): Observable<EntityResponseType> {
    return this.http.post<IVOrganization>(this.resourceUrl, vOrganization, { observe: 'response' });
  }

  update(vOrganization: IVOrganization): Observable<EntityResponseType> {
    return this.http.put<IVOrganization>(`${this.resourceUrl}/${this.getVOrganizationIdentifier(vOrganization)}`, vOrganization, {
      observe: 'response',
    });
  }

  partialUpdate(vOrganization: PartialUpdateVOrganization): Observable<EntityResponseType> {
    return this.http.patch<IVOrganization>(`${this.resourceUrl}/${this.getVOrganizationIdentifier(vOrganization)}`, vOrganization, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVOrganizationIdentifier(vOrganization: Pick<IVOrganization, 'id'>): number {
    return vOrganization.id;
  }

  compareVOrganization(o1: Pick<IVOrganization, 'id'> | null, o2: Pick<IVOrganization, 'id'> | null): boolean {
    return o1 && o2 ? this.getVOrganizationIdentifier(o1) === this.getVOrganizationIdentifier(o2) : o1 === o2;
  }

  addVOrganizationToCollectionIfMissing<Type extends Pick<IVOrganization, 'id'>>(
    vOrganizationCollection: Type[],
    ...vOrganizationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vOrganizations: Type[] = vOrganizationsToCheck.filter(isPresent);
    if (vOrganizations.length > 0) {
      const vOrganizationCollectionIdentifiers = vOrganizationCollection.map(
        vOrganizationItem => this.getVOrganizationIdentifier(vOrganizationItem)!
      );
      const vOrganizationsToAdd = vOrganizations.filter(vOrganizationItem => {
        const vOrganizationIdentifier = this.getVOrganizationIdentifier(vOrganizationItem);
        if (vOrganizationCollectionIdentifiers.includes(vOrganizationIdentifier)) {
          return false;
        }
        vOrganizationCollectionIdentifiers.push(vOrganizationIdentifier);
        return true;
      });
      return [...vOrganizationsToAdd, ...vOrganizationCollection];
    }
    return vOrganizationCollection;
  }
}
