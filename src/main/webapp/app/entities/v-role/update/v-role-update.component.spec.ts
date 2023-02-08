import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VRoleFormService } from './v-role-form.service';
import { VRoleService } from '../service/v-role.service';
import { IVRole } from '../v-role.model';

import { VRoleUpdateComponent } from './v-role-update.component';

describe('VRole Management Update Component', () => {
  let comp: VRoleUpdateComponent;
  let fixture: ComponentFixture<VRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vRoleFormService: VRoleFormService;
  let vRoleService: VRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VRoleUpdateComponent],
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
      .overrideTemplate(VRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vRoleFormService = TestBed.inject(VRoleFormService);
    vRoleService = TestBed.inject(VRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vRole: IVRole = { id: 456 };

      activatedRoute.data = of({ vRole });
      comp.ngOnInit();

      expect(comp.vRole).toEqual(vRole);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVRole>>();
      const vRole = { id: 123 };
      jest.spyOn(vRoleFormService, 'getVRole').mockReturnValue(vRole);
      jest.spyOn(vRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vRole }));
      saveSubject.complete();

      // THEN
      expect(vRoleFormService.getVRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vRoleService.update).toHaveBeenCalledWith(expect.objectContaining(vRole));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVRole>>();
      const vRole = { id: 123 };
      jest.spyOn(vRoleFormService, 'getVRole').mockReturnValue({ id: null });
      jest.spyOn(vRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vRole }));
      saveSubject.complete();

      // THEN
      expect(vRoleFormService.getVRole).toHaveBeenCalled();
      expect(vRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVRole>>();
      const vRole = { id: 123 };
      jest.spyOn(vRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
