package com.subbu.askdoc.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class QdrantCollectionInitializer {

    private final AppProperties props;

    @PostConstruct
    public void init() {

        if (!props.vectorstore().qdrant().initializeSchema()) {
            log.info("Qdrant schema initialization is disabled");
            return;
        }

        String host = props.vectorstore().qdrant().host();
        String collection = props.vectorstore().qdrant().collectionName();

        String url = "http://" + host + ":6333/collections/" + collection;

        RestTemplate restTemplate = new RestTemplate();
        String provider = props.llm().provider();

        int dimension = provider.equalsIgnoreCase("ollama")
                ? props.ollama().embeddingDimension()
                : props.openai().embeddingDimension();

        try {
            String body = """
            {
              "vectors": {
                "size": %d,
                "distance": "Cosine"
              }
            }
            """.formatted(dimension);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

            log.info("Qdrant collection '{}' created successfully", collection);

        } catch (HttpClientErrorException e) {

            // 🔥 HANDLE 409 HERE
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                log.info("Qdrant collection '{}' already exists. Skipping creation.", collection);
            } else {
                log.error("Qdrant client error: {}", e.getResponseBodyAsString(), e);
                throw new RuntimeException("Qdrant initialization failed", e);
            }

        } catch (Exception e) {
            log.error("Unexpected error initializing Qdrant", e);
            throw new RuntimeException("Qdrant initialization failed", e);
        }
    }
}