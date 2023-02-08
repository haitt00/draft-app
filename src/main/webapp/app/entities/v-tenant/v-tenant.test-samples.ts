import { IVTenant, NewVTenant } from './v-tenant.model';

export const sampleWithRequiredData: IVTenant = {
  id: 82345,
};

export const sampleWithPartialData: IVTenant = {
  id: 11361,
  name: 'real-time',
  description: 'Agent Soft withdrawal',
};

export const sampleWithFullData: IVTenant = {
  id: 17308,
  code: 'mobile enterprise info-mediaries',
  name: 'invoice',
  description: 'JBOD',
  status: false,
};

export const sampleWithNewData: NewVTenant = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
