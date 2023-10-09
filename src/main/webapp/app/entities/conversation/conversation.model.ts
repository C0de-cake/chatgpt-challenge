export interface IConversation {
  id: number;
  name?: string | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
