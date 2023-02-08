import dayjs from 'dayjs/esm';

export interface IVAbstractAuditingEntity {
  id: number;
  createdBy?: string | null;
  createdTime?: dayjs.Dayjs | null;
  modifiedBy?: string | null;
  modifiedTime?: dayjs.Dayjs | null;
  delFlag?: boolean | null;
}

export type NewVAbstractAuditingEntity = Omit<IVAbstractAuditingEntity, 'id'> & { id: null };
