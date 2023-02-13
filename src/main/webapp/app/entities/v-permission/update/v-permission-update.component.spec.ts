import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VPermissionFormService } from './v-permission-form.service';
import { VPermissionService } from '../service/v-permission.service';
import { IVPermission } from '../v-permission.model';

import { VPermissionUpdateComponent } from './v-permission-update.component';

describe('VPermission Management Update Component', () => {
  let comp: VPermissionUpdateComponent;
  let fixture: ComponentFixture<VPermissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vPermissionFormService: VPermissionFormService;
  let vPermissionService: VPermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VPermissionUpdateComponent],
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
      .overrideTemplate(VPermissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VPermissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vPermissionFormService = TestBed.inject(VPermissionFormService);
    vPermissionService = TestBed.inject(VPermissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vPermission: IVPermission = { id: 456 };

      activatedRoute.data = of({ vPermission });
      comp.ngOnInit();

      expect(comp.vPermission).toEqual(vPermission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVPermission>>();
      const vPermission = { id: 123 };
      jest.spyOn(vPermissionFormService, 'getVPermission').mockReturnValue(vPermission);
      jest.spyOn(vPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vPermission }));
      saveSubject.complete();

      // THEN
      expect(vPermissionFormService.getVPermission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vPermissionService.update).toHaveBeenCalledWith(expect.objectContaining(vPermission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVPermission>>();
      const vPermission = { id: 123 };
      jest.spyOn(vPermissionFormService, 'getVPermission').mockReturnValue({ id: null });
      jest.spyOn(vPermissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vPermission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vPermission }));
      saveSubject.complete();

      // THEN
      expect(vPermissionFormService.getVPermission).toHaveBeenCalled();
      expect(vPermissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVPermission>>();
      const vPermission = { id: 123 };
      jest.spyOn(vPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vPermissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
