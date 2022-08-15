export interface IPendinglist {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  state?: boolean | null;
}

export const defaultValue: Readonly<IPendinglist> = {
  state: false,
};
