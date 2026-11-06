package com.insurance.vector.search.service;

import com.insurance.vector.search.model.DocumentRequest;
import com.insurance.vector.search.repository.LyricSearchRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter.Expression;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for handling lyric search operations.
 */
@Service
public class LyricSearchService {

    private static final int MAX_TOKENS = (int) (8192 * 0.80); // OpenAI model's maximum content length + BUFFER for when one word > 1 token

    @Autowired
    private LyricSearchRepository lyricSearchRepository;

    /**
     * Adds validated documents to the repository after filtering out null or excessively long documents.
     *
     * @param documents List of document requests to be added
     * @return List of documents that were successfully added
     */
    public List<Document> addDocuments(List<DocumentRequest> documents) {
        if (documents == null || documents.isEmpty()) {
            return Collections.emptyList();
        }

        List<Document> docs = documents.stream()
                .filter(doc -> doc != null && doc.getContent() != null && !doc.getContent()
                        .trim()
                        .isEmpty())
                .map(doc -> new Document(doc.getContent(), doc.getMetadata()))
                .filter(doc -> {
                    int wordCount = doc.getText()
                            .split("\\s+").length;
                    return wordCount <= MAX_TOKENS;
                })
                .collect(Collectors.toList());

        if (!docs.isEmpty()) {
            lyricSearchRepository.addDocuments(docs);
        }

        return docs;
    }

    /**
     * Deletes documents from the repository based on the provided document IDs.
     *
     * @param ids List of document IDs to be deleted
     * @return List of successfully deleted document IDs, or an empty list if deletion was unsuccessful
     */
    public void deleteDocuments(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        try {
            lyricSearchRepository.deleteDocuments(ids);
        } catch (final Exception e) {
            throw new RuntimeException("Exception occurred while deleting the documents");
        }
    }

    /**
     * Performs a semantic search on documents based on the given query, with specified top results and similarity threshold.
     *
     * @param query               The search query
     * @param topK                The number of top results to return
     * @param similarityThreshold The minimum similarity score for results to be included
     * @return List of search results containing document content and metadata
     */
    public List<Map<String, Object>> searchDocuments(final String query, final int topK, final double similarityThreshold) {
        final SearchRequest searchRequest = SearchRequest.builder().query(query).topK(topK).similarityThreshold(similarityThreshold).build();
        final List<Document> results = lyricSearchRepository.semanticSearchByLyrics(searchRequest);

        return results.stream()
                .map(doc -> {
                    assert doc.getText() != null;
                    return Map.of("content", doc.getText(), "metadata", doc.getMetadata());
                })
                .collect(Collectors.toList());
    }

    /**
     * Searches documents using a metadata filter, such as filtering by artist, alongside the given query.
     *
     * @param query               The search query
     * @param topK                The number of top results to return
     * @param similarityThreshold The minimum similarity score for results to be included
     * @param artist              The artist to filter results by
     * @return List of filtered search results containing document content and metadata
     */
    public List<Map<String, Object>> searchDocumentsWithFilter(String query, int topK, double similarityThreshold, String artist) {
        FilterExpressionBuilder filterBuilder = new FilterExpressionBuilder();
        Expression filterExpression = filterBuilder.eq("artist", artist)
                .build();

        final SearchRequest searchRequest = SearchRequest.builder().query(query)
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .filterExpression(filterExpression)
                .build();

        List<Document> results = lyricSearchRepository.semanticSearchByLyrics(searchRequest);

        return results.stream()
                .map(doc -> {
                    assert doc.getText() != null;
                    return Map.of("content", doc.getText(), "metadata", doc.getMetadata());
                })
                .collect(Collectors.toList());
    }

    /**
     * Gets the status of the application.
     *
     * @return The application status
     */
    public String getStatus() {
        return "Application is running!";
    }
}
