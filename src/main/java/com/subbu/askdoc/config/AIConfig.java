package com.subbu.askdoc.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AIConfig {

    private final AppProperties props;

    @Bean
    public ChatModel chatModel() {
        if ("ollama".equalsIgnoreCase(props.llm().provider())) {
            return OllamaChatModel.builder()
                    .baseUrl(props.ollama().baseUrl())
                    .modelName(props.ollama().chatModel())
                    .temperature(0.2)
                    .build();
        }

        return OpenAiChatModel.builder()
                .apiKey(props.openai().apiKey())
                .modelName(props.openai().chatModel())
                .temperature(0.2)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        if ("ollama".equalsIgnoreCase(props.llm().provider())) {
            return OllamaEmbeddingModel.builder()
                    .baseUrl(props.ollama().baseUrl())
                    .modelName(props.ollama().embeddingModel())
                    .build();
        }

        return OpenAiEmbeddingModel.builder()
                .apiKey(props.openai().apiKey())
                .modelName(props.openai().embeddingModel())
                .build();
    }
}