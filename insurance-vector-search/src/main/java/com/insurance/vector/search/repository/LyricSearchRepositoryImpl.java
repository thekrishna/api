package com.insurance.vector.search.repository;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LyricSearchRepositoryImpl implements LyricSearchRepository {

    private final VectorStore vectorStore;

    @Autowired
    public LyricSearchRepositoryImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void addDocuments(List<Document> docs) {
        vectorStore.add(docs);
    }

    @Override
    public void deleteDocuments(List<String> ids) {
        vectorStore.delete(ids);
    }

    @Override
    public List<Document> semanticSearchByLyrics(SearchRequest searchRequest) {
        return vectorStore.similaritySearch(searchRequest);
    }
}