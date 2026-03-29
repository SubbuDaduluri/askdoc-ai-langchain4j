package com.subbu.askdoc.service.impl;

import com.subbu.askdoc.config.AppProperties;
import com.subbu.askdoc.service.IngestionService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionServiceImpl implements IngestionService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> store;
    private final AppProperties props;

    @Override
    public void ingest(MultipartFile file) {

        validate(file);

        File tempFile = null;

        try {
            // Save file temporarily
            tempFile = File.createTempFile("upload-", ".pdf");
            file.transferTo(tempFile);

            log.info("Uploading file: {}", file.getOriginalFilename());

            // Load PDF
            Document document = FileSystemDocumentLoader.loadDocument(
                    tempFile.toPath(),
                    new ApachePdfBoxDocumentParser()
            );

            // Add metadata (IMPORTANT)
            document.metadata().put("fileName", file.getOriginalFilename());

            // Store embeddings (CORRECT for your version)
            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .embeddingModel(embeddingModel)
                    .embeddingStore(store) // use injected store
                    .build();

            ingestor.ingest(document);

            log.info("Document ingested successfully");

        } catch (IOException e) {
            log.error("Failed to ingest document", e);
            throw new RuntimeException("Failed to ingest document", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String name = file.getOriginalFilename();
        if (name == null || !name.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are supported");
        }
    }
}