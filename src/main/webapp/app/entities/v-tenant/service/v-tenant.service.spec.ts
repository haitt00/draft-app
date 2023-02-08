import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVTenant } from '../v-tenant.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../v-tenant.test-samples';

import { VTenantService } from './v-tenant.service';

const requireRestSample: IVTenant = {
  ...sampleWithRequiredData,
};

describe('VTenant Service', () => {
  let service: VTenantService;
  let httpMock: HttpTestingController;
  let expectedResult: IVTenant | IVTenant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VTenantService);
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

    it('should create a VTenant', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vTenant = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vTenant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VTenant', () => {
      const vTenant = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vTenant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VTenant', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VTenant', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VTenant', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVTenantToCollectionIfMissing', () => {
      it('should add a VTenant to an empty array', () => {
        const vTenant: IVTenant = sampleWithRequiredData;
        expectedResult = service.addVTenantToCollectionIfMissing([], vTenant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vTenant);
      });

      it('should not add a VTenant to an array that contains it', () => {
        const vTenant: IVTenant = sampleWithRequiredData;
        const vTenantCollection: IVTenant[] = [
          {
            ...vTenant,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVTenantToCollectionIfMissing(vTenantCollection, vTenant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VTenant to an array that doesn't contain it", () => {
        const vTenant: IVTenant = sampleWithRequiredData;
        const vTenantCollection: IVTenant[] = [sampleWithPartialData];
        expectedResult = service.addVTenantToCollectionIfMissing(vTenantCollection, vTenant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vTenant);
      });

      it('should add only unique VTenant to an array', () => {
        const vTenantArray: IVTenant[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vTenantCollection: IVTenant[] = [sampleWithRequiredData];
        expectedResult = service.addVTenantToCollectionIfMissing(vTenantCollection, ...vTenantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vTenant: IVTenant = sampleWithRequiredData;
        const vTenant2: IVTenant = sampleWithPartialData;
        expectedResult = service.addVTenantToCollectionIfMissing([], vTenant, vTenant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vTenant);
        expect(expectedResult).toContain(vTenant2);
      });

      it('should accept null and undefined values', () => {
        const vTenant: IVTenant = sampleWithRequiredData;
        expectedResult = service.addVTenantToCollectionIfMissing([], null, vTenant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vTenant);
      });

      it('should return initial array if no VTenant is added', () => {
        const vTenantCollection: IVTenant[] = [sampleWithRequiredData];
        expectedResult = service.addVTenantToCollectionIfMissing(vTenantCollection, undefined, null);
        expect(expectedResult).toEqual(vTenantCollection);
      });
    });

    describe('compareVTenant', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVTenant(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVTenant(entity1, entity2);
        const compareResult2 = service.compareVTenant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVTenant(entity1, entity2);
        const compareResult2 = service.compareVTenant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVTenant(entity1, entity2);
        const compareResult2 = service.compareVTenant(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
