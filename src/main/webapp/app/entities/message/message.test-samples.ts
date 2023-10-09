import dayjs from 'dayjs/esm';

import { Owner } from 'app/entities/enumerations/owner.model';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 8193,
};

export const sampleWithPartialData: IMessage = {
  id: 16787,
  createdBy: 'Northwest Chair Human',
  createdDate: dayjs('2023-07-16T20:29'),
  lastModifiedBy: 'Developer',
};

export const sampleWithFullData: IMessage = {
  id: 25310,
  content: '../fake-data/blob/hipster.txt',
  owner: 'GPT',
  createdBy: 'impassioned violet',
  createdDate: dayjs('2023-07-16T18:52'),
  lastModifiedBy: 'card',
  lastModifiedDate: dayjs('2023-07-17T00:33'),
};

export const sampleWithNewData: NewMessage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
