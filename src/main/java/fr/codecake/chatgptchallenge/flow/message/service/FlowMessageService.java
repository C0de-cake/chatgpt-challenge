package fr.codecake.chatgptchallenge.flow.message.service;

import fr.codecake.chatgptchallenge.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class FlowMessageService {

    private final MessageService messageService;

    public FlowMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
