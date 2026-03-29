package com.subbu.askdoc.controller;

import com.subbu.askdoc.service.IngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final IngestionService ingestionService;

    public PdfController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    /**
     * Upload PDF and process into vector DB
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {

        // Basic validation
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if (!file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files allowed");
        }

        // Process file
        ingestionService.ingest(file);

        return ResponseEntity.ok("PDF uploaded & processed successfully");
    }
}