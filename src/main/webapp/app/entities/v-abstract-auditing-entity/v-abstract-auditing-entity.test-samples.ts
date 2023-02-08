import dayjs from 'dayjs/esm';

import { IVAbstractAuditingEntity, NewVAbstractAuditingEntity } from './v-abstract-auditing-entity.model';

export const sampleWithRequiredData: IVAbstractAuditingEntity = {
  id: 54830,
};

export const sampleWithPartialData: IVAbstractAuditingEntity = {
  id: 15042,
  createdBy: 'Computers Rubber',
  createdTime: dayjs('2023-02-07T21:11'),
  modifiedTime: dayjs('2023-02-07T12:52'),
  delFlag: false,
};

export const sampleWithFullData: IVAbstractAuditingEntity = {
  id: 87164,
  createdBy: 'Algerian Communications',
  createdTime: dayjs('2023-02-08T02:02'),
  modifiedBy: 'Mobility Radial',
  modifiedTime: dayjs('2023-02-07T12:18'),
  delFlag: true,
};

export const sampleWithNewData: NewVAbstractAuditingEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
