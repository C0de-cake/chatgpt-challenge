package fr.codecake.chatgptchallenge.flow.message.rest;

import fr.codecake.chatgptchallenge.flow.message.service.FlowMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FlowMessageResource {

    private final FlowMessageService flowMessageService;

    public FlowMessageResource(FlowMessageService flowMessageService) {
        this.flowMessageService = flowMessageService;
    }

}
