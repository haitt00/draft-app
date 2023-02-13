import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VPermissionService } from '../service/v-permission.service';

import { VPermissionComponent } from './v-permission.component';

describe('VPermission Management Component', () => {
  let comp: VPermissionComponent;
  let fixture: ComponentFixture<VPermissionComponent>;
  let service: VPermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'v-permission', component: VPermissionComponent }]), HttpClientTestingModule],
      declarations: [VPermissionComponent],
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
      .overrideTemplate(VPermissionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VPermissionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VPermissionService);

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
    expect(comp.vPermissions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to vPermissionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getVPermissionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getVPermissionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
