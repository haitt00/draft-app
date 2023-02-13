import { IVPermission, NewVPermission } from './v-permission.model';

export const sampleWithRequiredData: IVPermission = {
  id: 57379,
};

export const sampleWithPartialData: IVPermission = {
  id: 10332,
  code: 'Electronics Connecticut Generic',
  name: 'HDD Mandatory',
  description: 'regional Incredible Passage',
  url: 'https://evans.name',
  component: 'website reboot',
  perms: 'Associate Designer Supervisor',
};

export const sampleWithFullData: IVPermission = {
  id: 29396,
  parentId: 91544,
  code: 'expedite Tasty payment',
  name: 'deposit payment Account',
  description: 'Cross-platform',
  type: 'transmitter',
  order: 95260,
  url: 'http://rashawn.org',
  component: 'orange channels',
  perms: 'Rubber Tools',
};

export const sampleWithNewData: NewVPermission = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
