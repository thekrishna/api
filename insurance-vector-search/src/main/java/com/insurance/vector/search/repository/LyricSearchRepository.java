package com.insurance.vector.search.repository;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;

public interface LyricSearchRepository {

    void addDocuments(List<Document> docs);

    void deleteDocuments(List<String> ids);

    List<Document> semanticSearchByLyrics(SearchRequest searchRequest);
}