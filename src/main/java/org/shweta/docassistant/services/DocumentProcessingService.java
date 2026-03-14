package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentProcessingService {

    private final PdfService pdfService;
    private final TextChunkService textChunkService;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    public void processPdf(Long paperId, String fileName) {

        String text = pdfService.extractText(fileName);

        List<String> chunks = textChunkService.chunkText(text);

        for (String chunk : chunks) {
            // Change from float[] to List<Double> to match EmbeddingService
            List<Double> embedding = embeddingService.createEmbedding(chunk);

            // This call must now accept List<Double>
            vectorStoreService.saveChunk(chunk, embedding, Math.toIntExact(paperId));
        }
    }
}
