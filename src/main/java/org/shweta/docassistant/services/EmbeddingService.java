package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public List<Double> createEmbedding(String text) {
        // 1. Get the float array from the model
        float[] embeddingArray = embeddingModel.embed(text);

        System.out.println("Embedding size: " + embeddingArray.length);

        // 2. Manually convert float[] to List<Double>
        List<Double> list = new ArrayList<>(embeddingArray.length);
        for (float f : embeddingArray) {
            list.add((double) f);
        }

        return list;
    }
}