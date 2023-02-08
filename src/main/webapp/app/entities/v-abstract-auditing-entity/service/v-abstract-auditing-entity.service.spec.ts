import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../v-abstract-auditing-entity.test-samples';

import { VAbstractAuditingEntityService, RestVAbstractAuditingEntity } from './v-abstract-auditing-entity.service';

const requireRestSample: RestVAbstractAuditingEntity = {
  ...sampleWithRequiredData,
  createdTime: sampleWithRequiredData.createdTime?.toJSON(),
  modifiedTime: sampleWithRequiredData.modifiedTime?.toJSON(),
};

describe('VAbstractAuditingEntity Service', () => {
  let service: VAbstractAuditingEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IVAbstractAuditingEntity | IVAbstractAuditingEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VAbstractAuditingEntityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a VAbstractAuditingEntity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vAbstractAuditingEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vAbstractAuditingEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VAbstractAuditingEntity', () => {
      const vAbstractAuditingEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vAbstractAuditingEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VAbstractAuditingEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VAbstractAuditingEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VAbstractAuditingEntity', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVAbstractAuditingEntityToCollectionIfMissing', () => {
      it('should add a VAbstractAuditingEntity to an empty array', () => {
        const vAbstractAuditingEntity: IVAbstractAuditingEntity = sampleWithRequiredData;
        expectedResult = service.addVAbstractAuditingEntityToCollectionIfMissing([], vAbstractAuditingEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vAbstractAuditingEntity);
      });

      it('should not add a VAbstractAuditingEntity to an array that contains it', () => {
        const vAbstractAuditingEntity: IVAbstractAuditingEntity = sampleWithRequiredData;
        const vAbstractAuditingEntityCollection: IVAbstractAuditingEntity[] = [
          {
            ...vAbstractAuditingEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVAbstractAuditingEntityToCollectionIfMissing(
          vAbstractAuditingEntityCollection,
          vAbstractAuditingEntity
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VAbstractAuditingEntity to an array that doesn't contain it", () => {
        const vAbstractAuditingEntity: IVAbstractAuditingEntity = sampleWithRequiredData;
        const vAbstractAuditingEntityCollection: IVAbstractAuditingEntity[] = [sampleWithPartialData];
        expectedResult = service.addVAbstractAuditingEntityToCollectionIfMissing(
          vAbstractAuditingEntityCollection,
          vAbstractAuditingEntity
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vAbstractAuditingEntity);
      });

      it('should add only unique VAbstractAuditingEntity to an array', () => {
        const vAbstractAuditingEntityArray: IVAbstractAuditingEntity[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const vAbstractAuditingEntityCollection: IVAbstractAuditingEntity[] = [sampleWithRequiredData];
        expectedResult = service.addVAbstractAuditingEntityToCollectionIfMissing(
          vAbstractAuditingEntityCollection,
          ...vAbstractAuditingEntityArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vAbstractAuditingEntity: IVAbstractAuditingEntity = sampleWithRequiredData;
        const vAbstractAuditingEntity2: IVAbstractAuditingEntity = sampleWithPartialData;
        expectedResult = service.addVAbstractAuditingEntityToCollectionIfMissing([], vAbstractAuditingEntity, vAbstractAuditingEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vAbstractAuditingEntity);
        expect(expectedResult).toContain(vAbstractAuditingEntity2);
      });

      it('should accept null and undefined values', () => {
        const vAbstractAuditingEntity: IVAbstractAuditingEntity = sampleWithRequiredData;
        expectedResult = service.addVAbstractAuditingEntityToCollectionIfMissing([], null, vAbstractAuditingEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vAbstractAuditingEntity);
      });

      it('should return initial array if no VAbstractAuditingEntity is added', () => {
        const vAbstractAuditingEntityCollection: IVAbstractAuditingEntity[] = [sampleWithRequiredData];
        expectedResult = service.addVAbstractAuditingEntityToCollectionIfMissing(vAbstractAuditingEntityCollection, undefined, null);
        expect(expectedResult).toEqual(vAbstractAuditingEntityCollection);
      });
    });

    describe('compareVAbstractAuditingEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVAbstractAuditingEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVAbstractAuditingEntity(entity1, entity2);
        const compareResult2 = service.compareVAbstractAuditingEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVAbstractAuditingEntity(entity1, entity2);
        const compareResult2 = service.compareVAbstractAuditingEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVAbstractAuditingEntity(entity1, entity2);
        const compareResult2 = service.compareVAbstractAuditingEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
