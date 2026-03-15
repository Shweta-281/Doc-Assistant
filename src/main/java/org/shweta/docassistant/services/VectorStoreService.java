package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorStoreService {

    private final JdbcTemplate jdbcTemplate;

    // Ensure this parameter is 'Long paperId'
    public void saveChunk(String content, List<Double> embedding, Integer paperId, String fileName) {

        String vectorString = embedding.toString();

        String sql = """
                INSERT INTO document_chunks (content, embedding, paper_id, file_name)
                VALUES (?, ?::vector, ?, ?)
                """;

        // JDBC will correctly map the Long to the integer column
        jdbcTemplate.update(sql, content, vectorString, paperId, fileName);
    }
}
