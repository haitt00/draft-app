import { IVUser } from 'app/entities/v-user/v-user.model';
import { IVRole } from 'app/entities/v-role/v-role.model';

export interface IVOrganization {
  id: number;
  parentId?: number | null;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  path?: string | null;
  fullPath?: string | null;
  enabled?: boolean | null;
  type?: number | null;
  vUsers?: Pick<IVUser, 'id'>[] | null;
  vRoles?: Pick<IVRole, 'id'>[] | null;
}

export type NewVOrganization = Omit<IVOrganization, 'id'> & { id: null };
