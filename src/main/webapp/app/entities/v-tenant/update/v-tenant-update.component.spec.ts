import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VTenantFormService } from './v-tenant-form.service';
import { VTenantService } from '../service/v-tenant.service';
import { IVTenant } from '../v-tenant.model';

import { VTenantUpdateComponent } from './v-tenant-update.component';

describe('VTenant Management Update Component', () => {
  let comp: VTenantUpdateComponent;
  let fixture: ComponentFixture<VTenantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vTenantFormService: VTenantFormService;
  let vTenantService: VTenantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VTenantUpdateComponent],
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
      .overrideTemplate(VTenantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VTenantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vTenantFormService = TestBed.inject(VTenantFormService);
    vTenantService = TestBed.inject(VTenantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vTenant: IVTenant = { id: 456 };

      activatedRoute.data = of({ vTenant });
      comp.ngOnInit();

      expect(comp.vTenant).toEqual(vTenant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVTenant>>();
      const vTenant = { id: 123 };
      jest.spyOn(vTenantFormService, 'getVTenant').mockReturnValue(vTenant);
      jest.spyOn(vTenantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vTenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vTenant }));
      saveSubject.complete();

      // THEN
      expect(vTenantFormService.getVTenant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vTenantService.update).toHaveBeenCalledWith(expect.objectContaining(vTenant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVTenant>>();
      const vTenant = { id: 123 };
      jest.spyOn(vTenantFormService, 'getVTenant').mockReturnValue({ id: null });
      jest.spyOn(vTenantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vTenant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vTenant }));
      saveSubject.complete();

      // THEN
      expect(vTenantFormService.getVTenant).toHaveBeenCalled();
      expect(vTenantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVTenant>>();
      const vTenant = { id: 123 };
      jest.spyOn(vTenantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vTenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vTenantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
