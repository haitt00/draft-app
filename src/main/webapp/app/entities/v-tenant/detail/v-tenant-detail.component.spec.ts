import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VTenantDetailComponent } from './v-tenant-detail.component';

describe('VTenant Management Detail Component', () => {
  let comp: VTenantDetailComponent;
  let fixture: ComponentFixture<VTenantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VTenantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vTenant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VTenantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VTenantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vTenant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vTenant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
