package com.insurance.vector.search.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public EmbeddingService(final EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public List<Double> generateEmbedding(final String text) {
        final float[] vector = embeddingModel.embed(text);
        return IntStream.range(0, vector.length)
                .mapToDouble(i -> vector[i])
                .boxed()
                .toList();
    }
}