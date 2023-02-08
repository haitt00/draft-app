import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VAbstractAuditingEntityDetailComponent } from './v-abstract-auditing-entity-detail.component';

describe('VAbstractAuditingEntity Management Detail Component', () => {
  let comp: VAbstractAuditingEntityDetailComponent;
  let fixture: ComponentFixture<VAbstractAuditingEntityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VAbstractAuditingEntityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vAbstractAuditingEntity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VAbstractAuditingEntityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VAbstractAuditingEntityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vAbstractAuditingEntity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vAbstractAuditingEntity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
