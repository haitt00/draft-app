export interface IVPermission {
  id: number;
  parentId?: number | null;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  type?: string | null;
  order?: number | null;
  url?: string | null;
  component?: string | null;
  perms?: string | null;
}

export type NewVPermission = Omit<IVPermission, 'id'> & { id: null };
