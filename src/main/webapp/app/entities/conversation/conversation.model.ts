export interface IConversation {
  id: number;
  name?: string | null;
  publicId?: string | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
