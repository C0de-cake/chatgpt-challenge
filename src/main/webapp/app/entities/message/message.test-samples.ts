import { Owner } from 'app/entities/enumerations/owner.model';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 23286,
};

export const sampleWithPartialData: IMessage = {
  id: 4370,
};

export const sampleWithFullData: IMessage = {
  id: 17126,
  content: '../fake-data/blob/hipster.txt',
  owner: 'USER',
};

export const sampleWithNewData: NewMessage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
