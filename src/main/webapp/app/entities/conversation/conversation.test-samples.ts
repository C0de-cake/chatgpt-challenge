import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 19776,
};

export const sampleWithPartialData: IConversation = {
  id: 29629,
  name: 'Human Alabama',
  publicId: '8e24b311-d1af-4b09-bc84-2660d54a395c',
};

export const sampleWithFullData: IConversation = {
  id: 12434,
  name: 'Bicycle auxiliary why',
  publicId: '4b4f6722-aae8-4984-80dc-e9077e4e5e7b',
};

export const sampleWithNewData: NewConversation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
