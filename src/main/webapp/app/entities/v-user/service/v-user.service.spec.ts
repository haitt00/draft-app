import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVUser } from '../v-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../v-user.test-samples';

import { VUserService } from './v-user.service';

const requireRestSample: IVUser = {
  ...sampleWithRequiredData,
};

describe('VUser Service', () => {
  let service: VUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IVUser | IVUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VUserService);
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

    it('should create a VUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VUser', () => {
      const vUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVUserToCollectionIfMissing', () => {
      it('should add a VUser to an empty array', () => {
        const vUser: IVUser = sampleWithRequiredData;
        expectedResult = service.addVUserToCollectionIfMissing([], vUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vUser);
      });

      it('should not add a VUser to an array that contains it', () => {
        const vUser: IVUser = sampleWithRequiredData;
        const vUserCollection: IVUser[] = [
          {
            ...vUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVUserToCollectionIfMissing(vUserCollection, vUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VUser to an array that doesn't contain it", () => {
        const vUser: IVUser = sampleWithRequiredData;
        const vUserCollection: IVUser[] = [sampleWithPartialData];
        expectedResult = service.addVUserToCollectionIfMissing(vUserCollection, vUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vUser);
      });

      it('should add only unique VUser to an array', () => {
        const vUserArray: IVUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vUserCollection: IVUser[] = [sampleWithRequiredData];
        expectedResult = service.addVUserToCollectionIfMissing(vUserCollection, ...vUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vUser: IVUser = sampleWithRequiredData;
        const vUser2: IVUser = sampleWithPartialData;
        expectedResult = service.addVUserToCollectionIfMissing([], vUser, vUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vUser);
        expect(expectedResult).toContain(vUser2);
      });

      it('should accept null and undefined values', () => {
        const vUser: IVUser = sampleWithRequiredData;
        expectedResult = service.addVUserToCollectionIfMissing([], null, vUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vUser);
      });

      it('should return initial array if no VUser is added', () => {
        const vUserCollection: IVUser[] = [sampleWithRequiredData];
        expectedResult = service.addVUserToCollectionIfMissing(vUserCollection, undefined, null);
        expect(expectedResult).toEqual(vUserCollection);
      });
    });

    describe('compareVUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVUser(entity1, entity2);
        const compareResult2 = service.compareVUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVUser(entity1, entity2);
        const compareResult2 = service.compareVUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVUser(entity1, entity2);
        const compareResult2 = service.compareVUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
