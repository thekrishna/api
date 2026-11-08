package com.insurance.vector.search.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.google.genai.embedding.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@SpringBootConfiguration
@EnableAutoConfiguration
public class Config {
    @Value("${spring.ai.google.genai.api-key}")
    private String googleApiKey;

    @Value("${spring.ai.vectorstore.mongodb.collection-name}")
    private String collectionName;

    @Value("${spring.ai.vectorstore.mongodb.index-name}")
    private String indexName;

    @Value("${spring.ai.vectorstore.mongodb.initialize-schema}")
    private boolean initializeSchema;

    @Bean
    public EmbeddingModel embeddingModel() {
        GoogleGenAiEmbeddingConnectionDetails connectionDetails =
                GoogleGenAiEmbeddingConnectionDetails.builder()
                        .apiKey(googleApiKey)
                        .build();

        GoogleGenAiTextEmbeddingOptions options =
                GoogleGenAiTextEmbeddingOptions.builder()
                        .model("gemini-embedding-001")
                        .taskType(GoogleGenAiTextEmbeddingOptions.TaskType.RETRIEVAL_DOCUMENT)
                        .build();

        return new GoogleGenAiTextEmbeddingModel(
                connectionDetails,
                options
        );
    }

    @Bean
    public VectorStore mongodbVectorStore(
            MongoTemplate mongoTemplate,
            EmbeddingModel embeddingModel) {

        return MongoDBAtlasVectorStore.builder(
                        mongoTemplate,
                        embeddingModel
                )
                .collectionName(collectionName)
                .vectorIndexName(indexName)
                .initializeSchema(initializeSchema)
                .build();
    }
}
