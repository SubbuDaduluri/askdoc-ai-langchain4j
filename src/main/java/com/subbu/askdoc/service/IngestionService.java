package com.subbu.askdoc.service;

import org.springframework.web.multipart.MultipartFile;

public interface IngestionService {
    public void ingest(MultipartFile file);
}
