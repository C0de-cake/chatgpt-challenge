import { Owner } from 'app/entities/enumerations/owner.model';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 7811,
};

export const sampleWithPartialData: IMessage = {
  id: 12081,
};

export const sampleWithFullData: IMessage = {
  id: 12310,
  content: 'loyalty syndicate robust',
  owner: 'GPT',
};

export const sampleWithNewData: NewMessage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
