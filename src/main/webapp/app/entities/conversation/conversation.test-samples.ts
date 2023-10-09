import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 9543,
};

export const sampleWithPartialData: IConversation = {
  id: 9788,
  name: 'Cloned Yuan',
};

export const sampleWithFullData: IConversation = {
  id: 29816,
  name: 'East Customizable boo',
};

export const sampleWithNewData: NewConversation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
