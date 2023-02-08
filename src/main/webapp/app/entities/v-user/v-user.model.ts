import { IVRole } from 'app/entities/v-role/v-role.model';
import { IVOrganization } from 'app/entities/v-organization/v-organization.model';

export interface IVUser {
  id: number;
  username?: string | null;
  fullname?: string | null;
  firstname?: string | null;
  lastname?: string | null;
  password?: string | null;
  email?: string | null;
  emailVerified?: string | null;
  language?: string | null;
  enabled?: boolean | null;
  vRoles?: Pick<IVRole, 'id'>[] | null;
  vOrganizations?: Pick<IVOrganization, 'id'>[] | null;
}

export type NewVUser = Omit<IVUser, 'id'> & { id: null };
