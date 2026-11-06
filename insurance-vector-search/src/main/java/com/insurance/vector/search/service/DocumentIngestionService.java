package com.insurance.vector.search.service;

import com.insurance.vector.search.model.InsuranceChunk;
import com.insurance.vector.search.model.OcrDocumentRequest;
import com.insurance.vector.search.model.OcrPage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class DocumentIngestionService {

    private final MongoTemplate mongoTemplate;
    private final TextChunkingService chunkingService;
    private final EmbeddingService embeddingService;

    public DocumentIngestionService(
            final MongoTemplate mongoTemplate,
            final TextChunkingService chunkingService,
            final EmbeddingService embeddingService) {

        this.mongoTemplate = mongoTemplate;
        this.chunkingService = chunkingService;
        this.embeddingService = embeddingService;
    }

    public void ingest(final OcrDocumentRequest request) {
        for (final OcrPage page : request.pages()) {
            final List<String> chunks = chunkingService.split(page.text());

            IntStream.range(0, chunks.size())
                    .forEach(index -> {

                        final String content = chunks.get(index);
                        final InsuranceChunk chunk = new InsuranceChunk();

                        chunk.setId(
                                request.documentId()
                                        + "-P" + page.pageNumber()
                                        + "-C" + (index + 1)
                        );

                        chunk.setDocumentId(request.documentId());
                        chunk.setPolicyId(request.policyId());
                        chunk.setApplicantId(request.applicantId());

                        chunk.setLob(request.lob());
                        chunk.setDocumentType(request.documentType());

                        chunk.setPageNumber(page.pageNumber());
                        chunk.setChunkNumber(index + 1);

                        chunk.setContent(content);

                        chunk.setEmbedding(embeddingService.generateEmbedding(content));

                        mongoTemplate.save(chunk);
                    });
        }
    }
}