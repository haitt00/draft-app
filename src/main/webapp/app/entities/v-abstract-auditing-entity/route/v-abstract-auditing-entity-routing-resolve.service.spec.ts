import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';
import { VAbstractAuditingEntityService } from '../service/v-abstract-auditing-entity.service';

import { VAbstractAuditingEntityRoutingResolveService } from './v-abstract-auditing-entity-routing-resolve.service';

describe('VAbstractAuditingEntity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VAbstractAuditingEntityRoutingResolveService;
  let service: VAbstractAuditingEntityService;
  let resultVAbstractAuditingEntity: IVAbstractAuditingEntity | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(VAbstractAuditingEntityRoutingResolveService);
    service = TestBed.inject(VAbstractAuditingEntityService);
    resultVAbstractAuditingEntity = undefined;
  });

  describe('resolve', () => {
    it('should return IVAbstractAuditingEntity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVAbstractAuditingEntity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVAbstractAuditingEntity).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVAbstractAuditingEntity = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVAbstractAuditingEntity).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IVAbstractAuditingEntity>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVAbstractAuditingEntity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVAbstractAuditingEntity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
