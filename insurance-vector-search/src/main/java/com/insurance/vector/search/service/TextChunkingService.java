package com.insurance.vector.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TextChunkingService {

    private static final int CHUNK_SIZE = 500;
    private static final int OVERLAP = 100;

    public List<String> split(String text) {

        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        String normalized = text.replaceAll("\\s+", " ").trim();

        int start = 0;

        while (start < normalized.length()) {

            int end = Math.min(
                    start + CHUNK_SIZE,
                    normalized.length()
            );

            chunks.add(normalized.substring(start, end));

            if (end == normalized.length()) {
                break;
            }

            start = end - OVERLAP;
        }

        return chunks;
    }
}