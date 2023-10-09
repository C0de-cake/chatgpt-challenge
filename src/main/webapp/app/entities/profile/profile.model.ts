import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { UserSubscription } from 'app/entities/enumerations/user-subscription.model';

export interface IProfile {
  id: number;
  subscription?: keyof typeof UserSubscription | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewProfile = Omit<IProfile, 'id'> & { id: null };
