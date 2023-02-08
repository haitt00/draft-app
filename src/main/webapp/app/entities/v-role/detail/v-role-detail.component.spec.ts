import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VRoleDetailComponent } from './v-role-detail.component';

describe('VRole Management Detail Component', () => {
  let comp: VRoleDetailComponent;
  let fixture: ComponentFixture<VRoleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VRoleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vRole: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VRoleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VRoleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vRole on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vRole).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
