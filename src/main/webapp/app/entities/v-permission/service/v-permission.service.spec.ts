import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVPermission } from '../v-permission.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../v-permission.test-samples';

import { VPermissionService } from './v-permission.service';

const requireRestSample: IVPermission = {
  ...sampleWithRequiredData,
};

describe('VPermission Service', () => {
  let service: VPermissionService;
  let httpMock: HttpTestingController;
  let expectedResult: IVPermission | IVPermission[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VPermissionService);
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

    it('should create a VPermission', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vPermission = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VPermission', () => {
      const vPermission = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VPermission', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VPermission', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VPermission', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVPermissionToCollectionIfMissing', () => {
      it('should add a VPermission to an empty array', () => {
        const vPermission: IVPermission = sampleWithRequiredData;
        expectedResult = service.addVPermissionToCollectionIfMissing([], vPermission);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vPermission);
      });

      it('should not add a VPermission to an array that contains it', () => {
        const vPermission: IVPermission = sampleWithRequiredData;
        const vPermissionCollection: IVPermission[] = [
          {
            ...vPermission,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVPermissionToCollectionIfMissing(vPermissionCollection, vPermission);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VPermission to an array that doesn't contain it", () => {
        const vPermission: IVPermission = sampleWithRequiredData;
        const vPermissionCollection: IVPermission[] = [sampleWithPartialData];
        expectedResult = service.addVPermissionToCollectionIfMissing(vPermissionCollection, vPermission);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vPermission);
      });

      it('should add only unique VPermission to an array', () => {
        const vPermissionArray: IVPermission[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vPermissionCollection: IVPermission[] = [sampleWithRequiredData];
        expectedResult = service.addVPermissionToCollectionIfMissing(vPermissionCollection, ...vPermissionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vPermission: IVPermission = sampleWithRequiredData;
        const vPermission2: IVPermission = sampleWithPartialData;
        expectedResult = service.addVPermissionToCollectionIfMissing([], vPermission, vPermission2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vPermission);
        expect(expectedResult).toContain(vPermission2);
      });

      it('should accept null and undefined values', () => {
        const vPermission: IVPermission = sampleWithRequiredData;
        expectedResult = service.addVPermissionToCollectionIfMissing([], null, vPermission, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vPermission);
      });

      it('should return initial array if no VPermission is added', () => {
        const vPermissionCollection: IVPermission[] = [sampleWithRequiredData];
        expectedResult = service.addVPermissionToCollectionIfMissing(vPermissionCollection, undefined, null);
        expect(expectedResult).toEqual(vPermissionCollection);
      });
    });

    describe('compareVPermission', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVPermission(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVPermission(entity1, entity2);
        const compareResult2 = service.compareVPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVPermission(entity1, entity2);
        const compareResult2 = service.compareVPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVPermission(entity1, entity2);
        const compareResult2 = service.compareVPermission(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
