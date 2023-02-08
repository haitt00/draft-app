import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVAbstractAuditingEntity, NewVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';

export type PartialUpdateVAbstractAuditingEntity = Partial<IVAbstractAuditingEntity> & Pick<IVAbstractAuditingEntity, 'id'>;

type RestOf<T extends IVAbstractAuditingEntity | NewVAbstractAuditingEntity> = Omit<T, 'createdTime' | 'modifiedTime'> & {
  createdTime?: string | null;
  modifiedTime?: string | null;
};

export type RestVAbstractAuditingEntity = RestOf<IVAbstractAuditingEntity>;

export type NewRestVAbstractAuditingEntity = RestOf<NewVAbstractAuditingEntity>;

export type PartialUpdateRestVAbstractAuditingEntity = RestOf<PartialUpdateVAbstractAuditingEntity>;

export type EntityResponseType = HttpResponse<IVAbstractAuditingEntity>;
export type EntityArrayResponseType = HttpResponse<IVAbstractAuditingEntity[]>;

@Injectable({ providedIn: 'root' })
export class VAbstractAuditingEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-abstract-auditing-entities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vAbstractAuditingEntity: NewVAbstractAuditingEntity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vAbstractAuditingEntity);
    return this.http
      .post<RestVAbstractAuditingEntity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vAbstractAuditingEntity: IVAbstractAuditingEntity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vAbstractAuditingEntity);
    return this.http
      .put<RestVAbstractAuditingEntity>(`${this.resourceUrl}/${this.getVAbstractAuditingEntityIdentifier(vAbstractAuditingEntity)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vAbstractAuditingEntity: PartialUpdateVAbstractAuditingEntity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vAbstractAuditingEntity);
    return this.http
      .patch<RestVAbstractAuditingEntity>(
        `${this.resourceUrl}/${this.getVAbstractAuditingEntityIdentifier(vAbstractAuditingEntity)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVAbstractAuditingEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVAbstractAuditingEntity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVAbstractAuditingEntityIdentifier(vAbstractAuditingEntity: Pick<IVAbstractAuditingEntity, 'id'>): number {
    return vAbstractAuditingEntity.id;
  }

  compareVAbstractAuditingEntity(
    o1: Pick<IVAbstractAuditingEntity, 'id'> | null,
    o2: Pick<IVAbstractAuditingEntity, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getVAbstractAuditingEntityIdentifier(o1) === this.getVAbstractAuditingEntityIdentifier(o2) : o1 === o2;
  }

  addVAbstractAuditingEntityToCollectionIfMissing<Type extends Pick<IVAbstractAuditingEntity, 'id'>>(
    vAbstractAuditingEntityCollection: Type[],
    ...vAbstractAuditingEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vAbstractAuditingEntities: Type[] = vAbstractAuditingEntitiesToCheck.filter(isPresent);
    if (vAbstractAuditingEntities.length > 0) {
      const vAbstractAuditingEntityCollectionIdentifiers = vAbstractAuditingEntityCollection.map(
        vAbstractAuditingEntityItem => this.getVAbstractAuditingEntityIdentifier(vAbstractAuditingEntityItem)!
      );
      const vAbstractAuditingEntitiesToAdd = vAbstractAuditingEntities.filter(vAbstractAuditingEntityItem => {
        const vAbstractAuditingEntityIdentifier = this.getVAbstractAuditingEntityIdentifier(vAbstractAuditingEntityItem);
        if (vAbstractAuditingEntityCollectionIdentifiers.includes(vAbstractAuditingEntityIdentifier)) {
          return false;
        }
        vAbstractAuditingEntityCollectionIdentifiers.push(vAbstractAuditingEntityIdentifier);
        return true;
      });
      return [...vAbstractAuditingEntitiesToAdd, ...vAbstractAuditingEntityCollection];
    }
    return vAbstractAuditingEntityCollection;
  }

  protected convertDateFromClient<T extends IVAbstractAuditingEntity | NewVAbstractAuditingEntity | PartialUpdateVAbstractAuditingEntity>(
    vAbstractAuditingEntity: T
  ): RestOf<T> {
    return {
      ...vAbstractAuditingEntity,
      createdTime: vAbstractAuditingEntity.createdTime?.toJSON() ?? null,
      modifiedTime: vAbstractAuditingEntity.modifiedTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVAbstractAuditingEntity: RestVAbstractAuditingEntity): IVAbstractAuditingEntity {
    return {
      ...restVAbstractAuditingEntity,
      createdTime: restVAbstractAuditingEntity.createdTime ? dayjs(restVAbstractAuditingEntity.createdTime) : undefined,
      modifiedTime: restVAbstractAuditingEntity.modifiedTime ? dayjs(restVAbstractAuditingEntity.modifiedTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVAbstractAuditingEntity>): HttpResponse<IVAbstractAuditingEntity> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVAbstractAuditingEntity[]>): HttpResponse<IVAbstractAuditingEntity[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
