export interface FlowMessageQueryDTO {
  content?: string | null;
  newConversation?: boolean | null;
  conversationPublicId?: string | null;
}

export interface FlowMessageResponseDTO {
  content?: string | null;
  conversationPublicId?: string | null;
  conversation: FlowConversationDTO | null;
}

export interface FlowConversationDTO {
  name?: string | null;
  publicId?: string | null;
}
