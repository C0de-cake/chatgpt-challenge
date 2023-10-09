import { IUser } from 'app/entities/user/user.model';
import { UserSubscription } from 'app/entities/enumerations/user-subscription.model';

export interface IProfile {
  id: number;
  subscription?: keyof typeof UserSubscription | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewProfile = Omit<IProfile, 'id'> & { id: null };
