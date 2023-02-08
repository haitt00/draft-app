import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VAbstractAuditingEntityFormService } from './v-abstract-auditing-entity-form.service';
import { VAbstractAuditingEntityService } from '../service/v-abstract-auditing-entity.service';
import { IVAbstractAuditingEntity } from '../v-abstract-auditing-entity.model';

import { VAbstractAuditingEntityUpdateComponent } from './v-abstract-auditing-entity-update.component';

describe('VAbstractAuditingEntity Management Update Component', () => {
  let comp: VAbstractAuditingEntityUpdateComponent;
  let fixture: ComponentFixture<VAbstractAuditingEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vAbstractAuditingEntityFormService: VAbstractAuditingEntityFormService;
  let vAbstractAuditingEntityService: VAbstractAuditingEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VAbstractAuditingEntityUpdateComponent],
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
      .overrideTemplate(VAbstractAuditingEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VAbstractAuditingEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vAbstractAuditingEntityFormService = TestBed.inject(VAbstractAuditingEntityFormService);
    vAbstractAuditingEntityService = TestBed.inject(VAbstractAuditingEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vAbstractAuditingEntity: IVAbstractAuditingEntity = { id: 456 };

      activatedRoute.data = of({ vAbstractAuditingEntity });
      comp.ngOnInit();

      expect(comp.vAbstractAuditingEntity).toEqual(vAbstractAuditingEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVAbstractAuditingEntity>>();
      const vAbstractAuditingEntity = { id: 123 };
      jest.spyOn(vAbstractAuditingEntityFormService, 'getVAbstractAuditingEntity').mockReturnValue(vAbstractAuditingEntity);
      jest.spyOn(vAbstractAuditingEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vAbstractAuditingEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vAbstractAuditingEntity }));
      saveSubject.complete();

      // THEN
      expect(vAbstractAuditingEntityFormService.getVAbstractAuditingEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vAbstractAuditingEntityService.update).toHaveBeenCalledWith(expect.objectContaining(vAbstractAuditingEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVAbstractAuditingEntity>>();
      const vAbstractAuditingEntity = { id: 123 };
      jest.spyOn(vAbstractAuditingEntityFormService, 'getVAbstractAuditingEntity').mockReturnValue({ id: null });
      jest.spyOn(vAbstractAuditingEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vAbstractAuditingEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vAbstractAuditingEntity }));
      saveSubject.complete();

      // THEN
      expect(vAbstractAuditingEntityFormService.getVAbstractAuditingEntity).toHaveBeenCalled();
      expect(vAbstractAuditingEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVAbstractAuditingEntity>>();
      const vAbstractAuditingEntity = { id: 123 };
      jest.spyOn(vAbstractAuditingEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vAbstractAuditingEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vAbstractAuditingEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
