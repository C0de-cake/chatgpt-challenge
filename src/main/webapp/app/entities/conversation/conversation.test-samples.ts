import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 12943,
};

export const sampleWithPartialData: IConversation = {
  id: 11069,
  name: 'Yuan',
};

export const sampleWithFullData: IConversation = {
  id: 29816,
  name: 'East Customizable boo',
  publicId: '023cbe66-be69-4e81-bb78-daa0aaafed75',
};

export const sampleWithNewData: NewConversation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
