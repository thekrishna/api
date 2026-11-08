package com.insurance.vector.search.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.vector.search.model.DocumentRequest;
import com.insurance.vector.search.service.LyricSearchService;

@RestController
public class LyricSearchController {

    @Autowired
    private LyricSearchService lyricSearchService;

    @PostMapping("/addDocuments")
    public List<Map<String, Object>> addDocuments(
            @RequestBody List<DocumentRequest> documents) {

        return lyricSearchService.addDocuments(documents)
                .stream()
                .map(doc -> {
                    assert doc.getText() != null;
                    return Map.<String, Object>of(
                            "content", doc.getText(),
                            "metadata", doc.getMetadata()
                    );
                })
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete")
    public void deleteDocuments(@RequestBody List<String> ids) {
        lyricSearchService.deleteDocuments(ids);
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchDocuments(@RequestParam String query,
                                                     @RequestParam int topK,
                                                     @RequestParam double similarityThreshold) {
        return lyricSearchService.searchDocuments(query, topK, similarityThreshold);

    }

    @GetMapping("/searchWithFilter")
    public List<Map<String, Object>> searchDocumentsWithFilter(@RequestParam String query,
                                                               @RequestParam int topK,
                                                               @RequestParam double similarityThreshold,
                                                               @RequestParam String artist) {
        return lyricSearchService.searchDocumentsWithFilter(query, topK, similarityThreshold, artist);
    }
}
