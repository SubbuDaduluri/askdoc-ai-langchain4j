package com.subbu.askdoc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Llm llm,
        Openai openai,
        Ollama ollama,
        Rag rag,
        Vectorstore vectorstore
) {

    public record Llm(String provider) {}

    public record Openai(
            String apiKey,
            String chatModel,
            String embeddingModel,
            int embeddingDimension
    ) {}

    public record Ollama(
            String baseUrl,
            String chatModel,
            String embeddingModel,
            int embeddingDimension
    ) {}

    public record Rag(int topK, int chunkSize, int chunkOverlap) {}

    public record Vectorstore(Qdrant qdrant) {}

    public record Qdrant(
            String host,
            int port,
            String collectionName,
            boolean initializeSchema
    ) {}
}