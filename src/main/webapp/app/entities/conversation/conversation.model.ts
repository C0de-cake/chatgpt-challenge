import dayjs from 'dayjs/esm';
import { IProfile } from 'app/entities/profile/profile.model';
import {IMessage} from "../message/message.model";

export interface IConversation {
  id: number;
  name?: string | null;
  publicId?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  profile?: Pick<IProfile, 'id'> | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };

export interface IConversationWithMessages {
  id: number;
  name?: string | null;
  publicId?: string | null;
  messages?: Array<IMessage>;
}
