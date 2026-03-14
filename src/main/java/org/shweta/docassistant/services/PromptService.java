package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final OllamaChatModel chatModel;
    private final EmbeddingService embeddingService;
    private final VectorSearchService vectorSearchService;

    public Flux<ChatResponse> callPromptService(String question) {

        // Convert question → embedding
        List<Double> embedding = embeddingService.createEmbedding(question);

        // Search similar chunks from pgvector
        List<String> chunks = vectorSearchService.searchSimilarChunks(embedding);
        // Build context
        String context = buildContext(chunks);

        // Create AI prompt
        String finalPrompt = String.format("""
        Answer the question using the context below.

        CONTEXT:
        %s

        QUESTION:
        %s
        """, context, question);

        // 5️⃣ Send to Ollama
        return chatModel.stream(new Prompt(finalPrompt));
    }

    private String buildContext(List<String> chunks){
        StringBuilder context = new StringBuilder();

        for(String chunk : chunks){
            context.append(chunk).append("\n\n");
        }

        return context.toString();

    }
}