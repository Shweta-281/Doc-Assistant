package org.shweta.docassistant.controllers;

import lombok.AllArgsConstructor;
import org.shweta.docassistant.dto.PromptDTO;
import org.shweta.docassistant.services.PromptService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/doc_assistant")
@AllArgsConstructor
public class PromptController {
    private final PromptService promptService;

    @PostMapping(value = "/prompt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getPrompt(@RequestBody PromptDTO promptDTO) {
        Flux<ChatResponse> response = promptService.callPromptService(promptDTO.prompt());
//        Flux<String> delayedResponse = response.delayElements(java.time.Duration.ofMillis(100));

        return response.map(chatResponse -> {
            String content = chatResponse.getResult().getOutput().getText().replace("\n", "*$#");
            return ServerSentEvent.builder(content).build();
        });
    }
}
