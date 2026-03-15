package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentProcessingService {

    private final PdfService pdfService;
    private final TextChunkService textChunkService;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;
    private final JdbcTemplate jdbcTemplate;

    public void processPdf(Long paperId, String fileName) {
        String text = pdfService.extractText(fileName);
        String[] chunks = text.split("(?<=\\G.{500})");

        for (String chunk : chunks) {
            List<Double> embedding = embeddingService.createEmbedding(chunk);

            // FIX: Convert the List to a String format that Postgres Vector understands
            String vectorString = embedding.toString();

            jdbcTemplate.update("""
                INSERT INTO document_chunks(content, embedding, file_name)
                VALUES (?, CAST(? AS vector), ?)
                """,
                    chunk,
                    vectorString, // Use the String here instead of the List
                    fileName
            );

            vectorStoreService.saveChunk(chunk, embedding, Math.toIntExact(paperId), fileName);
        }
    }
}
