import dayjs from 'dayjs/esm';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { Owner } from 'app/entities/enumerations/owner.model';

export interface IMessage {
  id: number;
  content?: string | null;
  owner?: keyof typeof Owner | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  conversation?: Pick<IConversation, 'id'> | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
