import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVUser, NewVUser } from '../v-user.model';

export type PartialUpdateVUser = Partial<IVUser> & Pick<IVUser, 'id'>;

export type EntityResponseType = HttpResponse<IVUser>;
export type EntityArrayResponseType = HttpResponse<IVUser[]>;

@Injectable({ providedIn: 'root' })
export class VUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vUser: NewVUser): Observable<EntityResponseType> {
    return this.http.post<IVUser>(this.resourceUrl, vUser, { observe: 'response' });
  }

  update(vUser: IVUser): Observable<EntityResponseType> {
    return this.http.put<IVUser>(`${this.resourceUrl}/${this.getVUserIdentifier(vUser)}`, vUser, { observe: 'response' });
  }

  partialUpdate(vUser: PartialUpdateVUser): Observable<EntityResponseType> {
    return this.http.patch<IVUser>(`${this.resourceUrl}/${this.getVUserIdentifier(vUser)}`, vUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVUserIdentifier(vUser: Pick<IVUser, 'id'>): number {
    return vUser.id;
  }

  compareVUser(o1: Pick<IVUser, 'id'> | null, o2: Pick<IVUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getVUserIdentifier(o1) === this.getVUserIdentifier(o2) : o1 === o2;
  }

  addVUserToCollectionIfMissing<Type extends Pick<IVUser, 'id'>>(
    vUserCollection: Type[],
    ...vUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vUsers: Type[] = vUsersToCheck.filter(isPresent);
    if (vUsers.length > 0) {
      const vUserCollectionIdentifiers = vUserCollection.map(vUserItem => this.getVUserIdentifier(vUserItem)!);
      const vUsersToAdd = vUsers.filter(vUserItem => {
        const vUserIdentifier = this.getVUserIdentifier(vUserItem);
        if (vUserCollectionIdentifiers.includes(vUserIdentifier)) {
          return false;
        }
        vUserCollectionIdentifiers.push(vUserIdentifier);
        return true;
      });
      return [...vUsersToAdd, ...vUserCollection];
    }
    return vUserCollection;
  }
}
