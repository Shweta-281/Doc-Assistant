package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final JdbcTemplate jdbcTemplate;

    // Use List<Double> to match your updated EmbeddingService
    public List<String> searchSimilarChunks(List<Double> embedding) {

        // Convert List<Double> to a PostgreSQL vector string format: [val1,val2,...]
        String vectorString = embedding.toString().replace(" ", "");

        String sql = """
                SELECT content
                FROM document_chunks
                ORDER BY embedding <-> ?::vector
                LIMIT 3
                """;

        // Pass the formatted string to the query
        return jdbcTemplate.queryForList(sql, String.class, vectorString);
    }
}
