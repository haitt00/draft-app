import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VUserDetailComponent } from './v-user-detail.component';

describe('VUser Management Detail Component', () => {
  let comp: VUserDetailComponent;
  let fixture: ComponentFixture<VUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
