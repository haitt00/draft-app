import { IVOrganization, NewVOrganization } from './v-organization.model';

export const sampleWithRequiredData: IVOrganization = {
  id: 5653,
};

export const sampleWithPartialData: IVOrganization = {
  id: 46149,
  parentId: 69331,
  description: 'leading-edge asynchronous Refined',
  path: 'challenge',
  fullPath: 'Illinois',
  enabled: true,
  type: 5975,
};

export const sampleWithFullData: IVOrganization = {
  id: 39452,
  parentId: 88478,
  code: 'Proactive Gourde',
  name: 'Cotton program hacking',
  description: 'Exclusive Frozen',
  path: 'Incredible',
  fullPath: 'Beauty',
  enabled: false,
  type: 83228,
};

export const sampleWithNewData: NewVOrganization = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
