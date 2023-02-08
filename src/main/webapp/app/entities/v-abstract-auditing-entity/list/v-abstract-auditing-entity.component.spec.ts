import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VAbstractAuditingEntityService } from '../service/v-abstract-auditing-entity.service';

import { VAbstractAuditingEntityComponent } from './v-abstract-auditing-entity.component';

describe('VAbstractAuditingEntity Management Component', () => {
  let comp: VAbstractAuditingEntityComponent;
  let fixture: ComponentFixture<VAbstractAuditingEntityComponent>;
  let service: VAbstractAuditingEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'v-abstract-auditing-entity', component: VAbstractAuditingEntityComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [VAbstractAuditingEntityComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(VAbstractAuditingEntityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VAbstractAuditingEntityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VAbstractAuditingEntityService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.vAbstractAuditingEntities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to vAbstractAuditingEntityService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getVAbstractAuditingEntityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getVAbstractAuditingEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
