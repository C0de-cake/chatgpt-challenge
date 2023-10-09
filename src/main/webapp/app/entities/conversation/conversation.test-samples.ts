import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 16132,
};

export const sampleWithPartialData: IConversation = {
  id: 4707,
  createdBy: 'female',
  createdDate: dayjs('2023-07-16T23:58'),
};

export const sampleWithFullData: IConversation = {
  id: 3473,
  name: 'Ohio past Borders',
  publicId: 'c842660d-54a3-495c-9f6f-caa70ef4b4f6',
  createdBy: 'Baby',
  createdDate: dayjs('2023-07-16T09:39'),
  lastModifiedBy: 'provident port West',
  lastModifiedDate: dayjs('2023-07-16T06:47'),
};

export const sampleWithNewData: NewConversation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
