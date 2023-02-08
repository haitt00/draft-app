import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VOrganizationDetailComponent } from './v-organization-detail.component';

describe('VOrganization Management Detail Component', () => {
  let comp: VOrganizationDetailComponent;
  let fixture: ComponentFixture<VOrganizationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VOrganizationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vOrganization: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VOrganizationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VOrganizationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vOrganization on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vOrganization).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
