import { IProfile } from 'app/entities/profile/profile.model';

export interface IConversation {
  id: number;
  name?: string | null;
  publicId?: string | null;
  profile?: Pick<IProfile, 'id'> | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
