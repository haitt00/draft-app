import { IVUser, NewVUser } from './v-user.model';

export const sampleWithRequiredData: IVUser = {
  id: 74957,
};

export const sampleWithPartialData: IVUser = {
  id: 58970,
  fullname: 'Malaysian',
  firstname: 'Ball stable Island',
  lastname: 'Manager Industrial',
  password: 'ROI SMTP',
  email: 'Brittany90@yahoo.com',
  language: 'Shoes program',
  enabled: false,
};

export const sampleWithFullData: IVUser = {
  id: 12345,
  username: 'Guam',
  fullname: 'Colombia',
  firstname: 'Fantastic',
  lastname: 'UIC-Franc administration Italy',
  password: 'Steel',
  email: 'Susana.Kilback25@hotmail.com',
  emailVerified: 'alliance',
  language: 'Enhanced',
  enabled: false,
};

export const sampleWithNewData: NewVUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
