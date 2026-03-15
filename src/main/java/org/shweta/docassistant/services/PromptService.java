package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.shweta.docassistant.dto.SourceChunk;
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
        List<SourceChunk> chunks = vectorSearchService.searchSimilarChunks(embedding);
        // Build context
        StringBuilder context = new StringBuilder();

        for(SourceChunk chunk : chunks) {
            context.append(chunk.getContent()).append("\n\n");
        }

        // Create AI prompt
        String finalPrompt = String.format("""
        Answer the question using the context below.

        CONTEXT:
        %s

        QUESTION:
        %s
        """, context, question);

        return chatModel.stream(new Prompt(finalPrompt));
    }
}