export interface IVTenant {
  id: number;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  status?: boolean | null;
}

export type NewVTenant = Omit<IVTenant, 'id'> & { id: null };
