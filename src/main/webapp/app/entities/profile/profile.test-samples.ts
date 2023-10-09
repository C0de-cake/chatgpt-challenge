import dayjs from 'dayjs/esm';

import { UserSubscription } from 'app/entities/enumerations/user-subscription.model';

import { IProfile, NewProfile } from './profile.model';

export const sampleWithRequiredData: IProfile = {
  id: 26239,
};

export const sampleWithPartialData: IProfile = {
  id: 17331,
  subscription: 'FREE',
  createdBy: 'Tunnel Intelligent',
  createdDate: dayjs('2023-07-27T06:38'),
  lastModifiedBy: 'Mercury bubble Male',
  lastModifiedDate: dayjs('2023-07-27T19:47'),
};

export const sampleWithFullData: IProfile = {
  id: 3255,
  subscription: 'PAID',
  createdBy: 'South Bedfordshire',
  createdDate: dayjs('2023-07-27T06:22'),
  lastModifiedBy: 'Sedan',
  lastModifiedDate: dayjs('2023-07-27T20:37'),
};

export const sampleWithNewData: NewProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
