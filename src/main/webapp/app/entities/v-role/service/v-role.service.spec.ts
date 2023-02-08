import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVRole } from '../v-role.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../v-role.test-samples';

import { VRoleService } from './v-role.service';

const requireRestSample: IVRole = {
  ...sampleWithRequiredData,
};

describe('VRole Service', () => {
  let service: VRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IVRole | IVRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VRoleService);
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

    it('should create a VRole', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VRole', () => {
      const vRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VRole', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVRoleToCollectionIfMissing', () => {
      it('should add a VRole to an empty array', () => {
        const vRole: IVRole = sampleWithRequiredData;
        expectedResult = service.addVRoleToCollectionIfMissing([], vRole);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vRole);
      });

      it('should not add a VRole to an array that contains it', () => {
        const vRole: IVRole = sampleWithRequiredData;
        const vRoleCollection: IVRole[] = [
          {
            ...vRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVRoleToCollectionIfMissing(vRoleCollection, vRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VRole to an array that doesn't contain it", () => {
        const vRole: IVRole = sampleWithRequiredData;
        const vRoleCollection: IVRole[] = [sampleWithPartialData];
        expectedResult = service.addVRoleToCollectionIfMissing(vRoleCollection, vRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vRole);
      });

      it('should add only unique VRole to an array', () => {
        const vRoleArray: IVRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vRoleCollection: IVRole[] = [sampleWithRequiredData];
        expectedResult = service.addVRoleToCollectionIfMissing(vRoleCollection, ...vRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vRole: IVRole = sampleWithRequiredData;
        const vRole2: IVRole = sampleWithPartialData;
        expectedResult = service.addVRoleToCollectionIfMissing([], vRole, vRole2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vRole);
        expect(expectedResult).toContain(vRole2);
      });

      it('should accept null and undefined values', () => {
        const vRole: IVRole = sampleWithRequiredData;
        expectedResult = service.addVRoleToCollectionIfMissing([], null, vRole, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vRole);
      });

      it('should return initial array if no VRole is added', () => {
        const vRoleCollection: IVRole[] = [sampleWithRequiredData];
        expectedResult = service.addVRoleToCollectionIfMissing(vRoleCollection, undefined, null);
        expect(expectedResult).toEqual(vRoleCollection);
      });
    });

    describe('compareVRole', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVRole(entity1, entity2);
        const compareResult2 = service.compareVRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVRole(entity1, entity2);
        const compareResult2 = service.compareVRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVRole(entity1, entity2);
        const compareResult2 = service.compareVRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
