import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VOrganizationFormService } from './v-organization-form.service';
import { VOrganizationService } from '../service/v-organization.service';
import { IVOrganization } from '../v-organization.model';
import { IVUser } from 'app/entities/v-user/v-user.model';
import { VUserService } from 'app/entities/v-user/service/v-user.service';
import { IVRole } from 'app/entities/v-role/v-role.model';
import { VRoleService } from 'app/entities/v-role/service/v-role.service';

import { VOrganizationUpdateComponent } from './v-organization-update.component';

describe('VOrganization Management Update Component', () => {
  let comp: VOrganizationUpdateComponent;
  let fixture: ComponentFixture<VOrganizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vOrganizationFormService: VOrganizationFormService;
  let vOrganizationService: VOrganizationService;
  let vUserService: VUserService;
  let vRoleService: VRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VOrganizationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VOrganizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VOrganizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vOrganizationFormService = TestBed.inject(VOrganizationFormService);
    vOrganizationService = TestBed.inject(VOrganizationService);
    vUserService = TestBed.inject(VUserService);
    vRoleService = TestBed.inject(VRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call VUser query and add missing value', () => {
      const vOrganization: IVOrganization = { id: 456 };
      const vUsers: IVUser[] = [{ id: 71268 }];
      vOrganization.vUsers = vUsers;

      const vUserCollection: IVUser[] = [{ id: 38038 }];
      jest.spyOn(vUserService, 'query').mockReturnValue(of(new HttpResponse({ body: vUserCollection })));
      const additionalVUsers = [...vUsers];
      const expectedCollection: IVUser[] = [...additionalVUsers, ...vUserCollection];
      jest.spyOn(vUserService, 'addVUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vOrganization });
      comp.ngOnInit();

      expect(vUserService.query).toHaveBeenCalled();
      expect(vUserService.addVUserToCollectionIfMissing).toHaveBeenCalledWith(
        vUserCollection,
        ...additionalVUsers.map(expect.objectContaining)
      );
      expect(comp.vUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call VRole query and add missing value', () => {
      const vOrganization: IVOrganization = { id: 456 };
      const vRoles: IVRole[] = [{ id: 16081 }];
      vOrganization.vRoles = vRoles;

      const vRoleCollection: IVRole[] = [{ id: 25390 }];
      jest.spyOn(vRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: vRoleCollection })));
      const additionalVRoles = [...vRoles];
      const expectedCollection: IVRole[] = [...additionalVRoles, ...vRoleCollection];
      jest.spyOn(vRoleService, 'addVRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vOrganization });
      comp.ngOnInit();

      expect(vRoleService.query).toHaveBeenCalled();
      expect(vRoleService.addVRoleToCollectionIfMissing).toHaveBeenCalledWith(
        vRoleCollection,
        ...additionalVRoles.map(expect.objectContaining)
      );
      expect(comp.vRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vOrganization: IVOrganization = { id: 456 };
      const vUser: IVUser = { id: 23521 };
      vOrganization.vUsers = [vUser];
      const vRole: IVRole = { id: 54245 };
      vOrganization.vRoles = [vRole];

      activatedRoute.data = of({ vOrganization });
      comp.ngOnInit();

      expect(comp.vUsersSharedCollection).toContain(vUser);
      expect(comp.vRolesSharedCollection).toContain(vRole);
      expect(comp.vOrganization).toEqual(vOrganization);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVOrganization>>();
      const vOrganization = { id: 123 };
      jest.spyOn(vOrganizationFormService, 'getVOrganization').mockReturnValue(vOrganization);
      jest.spyOn(vOrganizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vOrganization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vOrganization }));
      saveSubject.complete();

      // THEN
      expect(vOrganizationFormService.getVOrganization).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vOrganizationService.update).toHaveBeenCalledWith(expect.objectContaining(vOrganization));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVOrganization>>();
      const vOrganization = { id: 123 };
      jest.spyOn(vOrganizationFormService, 'getVOrganization').mockReturnValue({ id: null });
      jest.spyOn(vOrganizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vOrganization: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vOrganization }));
      saveSubject.complete();

      // THEN
      expect(vOrganizationFormService.getVOrganization).toHaveBeenCalled();
      expect(vOrganizationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVOrganization>>();
      const vOrganization = { id: 123 };
      jest.spyOn(vOrganizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vOrganization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vOrganizationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVUser', () => {
      it('Should forward to vUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vUserService, 'compareVUser');
        comp.compareVUser(entity, entity2);
        expect(vUserService.compareVUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVRole', () => {
      it('Should forward to vRoleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vRoleService, 'compareVRole');
        comp.compareVRole(entity, entity2);
        expect(vRoleService.compareVRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
