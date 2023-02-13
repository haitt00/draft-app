import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VPermissionDetailComponent } from './v-permission-detail.component';

describe('VPermission Management Detail Component', () => {
  let comp: VPermissionDetailComponent;
  let fixture: ComponentFixture<VPermissionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VPermissionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vPermission: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VPermissionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VPermissionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vPermission on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vPermission).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
