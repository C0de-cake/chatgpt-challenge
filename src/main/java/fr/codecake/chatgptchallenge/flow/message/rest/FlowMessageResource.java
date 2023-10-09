package fr.codecake.chatgptchallenge.flow.message.rest;

import fr.codecake.chatgptchallenge.flow.message.dto.FlowMessageQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.dto.FlowMessageResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.exception.OpenAIException;
import fr.codecake.chatgptchallenge.flow.message.service.FlowMessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/flow/message")
public class FlowMessageResource {
    private final Logger log = LoggerFactory.getLogger(FlowMessageResource.class);


    private final FlowMessageService flowMessageService;

    public FlowMessageResource(FlowMessageService flowMessageService) {
        this.flowMessageService = flowMessageService;
    }

    @PostMapping()
    public ResponseEntity<FlowMessageResponseDTO> sendMessage(@RequestBody @Valid FlowMessageQueryDTO flowMessageQueryDTO) {
        try {
            FlowMessageResponseDTO flowMessageResponseDTO = flowMessageService.sendMessage(flowMessageQueryDTO);
            return ResponseEntity.ok(flowMessageResponseDTO);
        } catch (OpenAIException oaie) {
            log.error("Something went wrong with this input {}", flowMessageQueryDTO, oaie);
            return ResponseEntity.internalServerError().build();
        }
    }
}
