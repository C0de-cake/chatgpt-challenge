import { UserSubscription } from 'app/entities/enumerations/user-subscription.model';

import { IProfile, NewProfile } from './profile.model';

export const sampleWithRequiredData: IProfile = {
  id: 2665,
};

export const sampleWithPartialData: IProfile = {
  id: 32007,
  subscription: 'PAID',
};

export const sampleWithFullData: IProfile = {
  id: 2207,
  subscription: 'PAID',
};

export const sampleWithNewData: NewProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
