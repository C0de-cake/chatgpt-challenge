import {Dayjs} from "dayjs";
import {IConversation} from "../entities/conversation/conversation.model";

export interface FlowMessageQueryDTO {
  content?: string | null;
  newConversation?: boolean | null;
  conversationPublicId?: string | null;
}

export interface FlowMessageResponseDTO {
  content?: string | null;
  conversationPublicId?: string | null;
  conversation?: IConversation | null;
}

export interface FlowConversationDTO {
  name?: string | null;
  publicId?: string | null;
  createdDate?: Dayjs | null;
}
