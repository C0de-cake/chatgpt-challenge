import { Owner } from 'app/entities/enumerations/owner.model';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 17002,
};

export const sampleWithPartialData: IMessage = {
  id: 9419,
};

export const sampleWithFullData: IMessage = {
  id: 8193,
  content: '../fake-data/blob/hipster.txt',
  owner: 'GPT',
};

export const sampleWithNewData: NewMessage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
