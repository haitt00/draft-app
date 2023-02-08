import { IVRole, NewVRole } from './v-role.model';

export const sampleWithRequiredData: IVRole = {
  id: 29017,
};

export const sampleWithPartialData: IVRole = {
  id: 16383,
};

export const sampleWithFullData: IVRole = {
  id: 96908,
  code: 'mission-critical grid-enabled',
  name: 'Shoes fresh-thinking transmitter',
  description: 'Refined SQL',
  applicationId: 88128,
};

export const sampleWithNewData: NewVRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
