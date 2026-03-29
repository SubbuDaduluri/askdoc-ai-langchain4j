package com.subbu.askdoc.service.impl;

import com.subbu.askdoc.config.AppProperties;
import com.subbu.askdoc.service.ChatService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> store;
    private final AppProperties props;

    @Override
    public String chat(String sessionId, String question) {

        // 1. Create retriever (correct)
        EmbeddingStoreContentRetriever retriever =
                EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(store)
                        .embeddingModel(embeddingModel)
                        .maxResults(props.rag().topK())
                        .build();

        // 2. Retrieve Content (NOT TextSegment)
        List<Content> contents = retriever.retrieve(Query.from(question));

        if (contents.isEmpty()) {
            return "No relevant information found.";
        }

        // 3. Extract text from Content → TextSegment
        String context = contents.stream()
                .map(content -> content.textSegment().text())
                .collect(Collectors.joining("\n"));

        // 4. Prompt
        String prompt = """
                Answer ONLY from the context below:
                
                Context:
                %s
                
                Question:
                %s
                
                If answer not found, say:
                "I could not find relevant information in the document."
                """.formatted(context, question);

        // 5. LLM call
        return chatModel.chat(prompt);
    }

}
