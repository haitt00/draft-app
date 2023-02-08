import { IVUser } from 'app/entities/v-user/v-user.model';
import { IVOrganization } from 'app/entities/v-organization/v-organization.model';

export interface IVRole {
  id: number;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  applicationId?: number | null;
  vUsers?: Pick<IVUser, 'id'>[] | null;
  vOrganizations?: Pick<IVOrganization, 'id'>[] | null;
}

export type NewVRole = Omit<IVRole, 'id'> & { id: null };
