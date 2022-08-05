import dayjs from 'dayjs';

export interface ISanctionlist {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  dob?: string | null;
  address?: string | null;
  passport?: string | null;
  score?: number | null;
}

export const defaultValue: Readonly<ISanctionlist> = {};
