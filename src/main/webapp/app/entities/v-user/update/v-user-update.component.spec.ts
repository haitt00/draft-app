import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VUserFormService } from './v-user-form.service';
import { VUserService } from '../service/v-user.service';
import { IVUser } from '../v-user.model';
import { IVRole } from 'app/entities/v-role/v-role.model';
import { VRoleService } from 'app/entities/v-role/service/v-role.service';

import { VUserUpdateComponent } from './v-user-update.component';

describe('VUser Management Update Component', () => {
  let comp: VUserUpdateComponent;
  let fixture: ComponentFixture<VUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vUserFormService: VUserFormService;
  let vUserService: VUserService;
  let vRoleService: VRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VUserUpdateComponent],
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
      .overrideTemplate(VUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vUserFormService = TestBed.inject(VUserFormService);
    vUserService = TestBed.inject(VUserService);
    vRoleService = TestBed.inject(VRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call VRole query and add missing value', () => {
      const vUser: IVUser = { id: 456 };
      const vRoles: IVRole[] = [{ id: 47691 }];
      vUser.vRoles = vRoles;

      const vRoleCollection: IVRole[] = [{ id: 1575 }];
      jest.spyOn(vRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: vRoleCollection })));
      const additionalVRoles = [...vRoles];
      const expectedCollection: IVRole[] = [...additionalVRoles, ...vRoleCollection];
      jest.spyOn(vRoleService, 'addVRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vUser });
      comp.ngOnInit();

      expect(vRoleService.query).toHaveBeenCalled();
      expect(vRoleService.addVRoleToCollectionIfMissing).toHaveBeenCalledWith(
        vRoleCollection,
        ...additionalVRoles.map(expect.objectContaining)
      );
      expect(comp.vRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vUser: IVUser = { id: 456 };
      const vRole: IVRole = { id: 65901 };
      vUser.vRoles = [vRole];

      activatedRoute.data = of({ vUser });
      comp.ngOnInit();

      expect(comp.vRolesSharedCollection).toContain(vRole);
      expect(comp.vUser).toEqual(vUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVUser>>();
      const vUser = { id: 123 };
      jest.spyOn(vUserFormService, 'getVUser').mockReturnValue(vUser);
      jest.spyOn(vUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vUser }));
      saveSubject.complete();

      // THEN
      expect(vUserFormService.getVUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vUserService.update).toHaveBeenCalledWith(expect.objectContaining(vUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVUser>>();
      const vUser = { id: 123 };
      jest.spyOn(vUserFormService, 'getVUser').mockReturnValue({ id: null });
      jest.spyOn(vUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vUser }));
      saveSubject.complete();

      // THEN
      expect(vUserFormService.getVUser).toHaveBeenCalled();
      expect(vUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVUser>>();
      const vUser = { id: 123 };
      jest.spyOn(vUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
