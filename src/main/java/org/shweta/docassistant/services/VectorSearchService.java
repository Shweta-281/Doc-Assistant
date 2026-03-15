package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.shweta.docassistant.dto.SourceChunk;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import com.pgvector.PGvector;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final JdbcTemplate jdbcTemplate;

    // Use List<Double> to match your updated EmbeddingService
    public List<SourceChunk> searchSimilarChunks(List<Double> embedding) {
        // Convert List<Double> to float array
        float[] floatArray = new float[embedding.size()];
        for (int i = 0; i < embedding.size(); i++) {
            floatArray[i] = embedding.get(i).floatValue();
        }

        String sql = "SELECT content, file_name FROM document_chunks ORDER BY embedding <-> ? LIMIT 3";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new SourceChunk(rs.getString("content"), rs.getString("file_name")),
                new PGvector(floatArray) // <--- This handles the "vector" type correctly
        );
    }
}
