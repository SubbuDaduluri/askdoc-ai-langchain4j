# 📄 AskDoc AI (LangChain4j + Ollama + Qdrant)

The **AskDoc AI** is a full-stack **RAG (Retrieval-Augmented Generation)** application that allows users to upload documents (PDF) and interactively ask questions about their content.

It leverages **Spring Boot 3 + LangChain4j + Ollama (local LLM) + Qdrant vector database** to provide fast, private, and context-aware responses.

---

## ✨ Features

- 📄 PDF Upload & Ingestion – Extracts and processes documents into embeddings
- 🧠 RAG Pipeline (LangChain4j) – Context-aware answers using vector retrieval
- 🤖 Local LLM Support (Ollama) – Fully offline, no API cost
- ☁️ OpenAI Support – Easily switch to cloud LLM with API key
- 🗃️ Qdrant Vector DB – High-performance similarity search
- 💬 Session-based Chat – Maintain user-specific conversations
- 🎯 Prompt Engineering – Structured and accurate responses
- ⚡ Flexible Architecture – Supports multi-model + multi-provider setup

---

## 🛠️ Technology Stack

| Component        | Technology                         | Role |
|-----------------|-----------------------------------|------|
| Backend         | Spring Boot 3, Java 21            | API, orchestration |
| AI Framework    | LangChain4j                       | RAG pipeline |
| LLM             | Ollama / OpenAI                   | Answer generation |
| Embedding Model | nomic-embed-text / OpenAI         | Text embeddings |
| Vector DB       | Qdrant                            | Stores embeddings |
| PDF Processing  | Apache PDFBox                     | Extracts text |

---

## 🚀 Getting Started

### Prerequisites

- Java 21
- Spring Boot 3.x
- Maven
- Docker (for Qdrant)
- Ollama installed locally

---

## 🧠 Setup Ollama (Local LLM)

ollama serve

ollama pull llama3  
ollama pull nomic-embed-text

---

## 🗃️ Start Qdrant

docker run -p 6333:6333 -p 6334:6334 qdrant/qdrant

---

## 🔑 OpenAI Setup (Optional)

Set your API key:

```bash
export OPENAI_API_KEY=your_api_key_here
```

Switch provider in `application.yaml`:

```yaml
app:
  llm:
    provider: openai
```

---

## ⚙️ Configuration (application.yaml)

```yaml
server:
  port: 8080

spring:
  application:
    name: askdoc-ai-langchain4j

app:
  llm:
    provider: ollama   # openai / ollama

  openai:
    api-key: ${OPENAI_API_KEY}
    chat-model: gpt-4o-mini
    embedding-model: text-embedding-3-small
    embedding-dimension: 1536

  ollama:
    base-url: http://localhost:11434
    chat-model: llama3
    embedding-model: nomic-embed-text
    embedding-dimension: 768

  rag:
    top-k: 5
    chunk-size: 1000
    chunk-overlap: 200

  vectorstore:
    qdrant:
      host: localhost
      port: 6334
      collection-name: askdoc
      initialize-schema: true
```

---

## ▶️ Run Application

./mvnw clean install  
./mvnw spring-boot:run

---

## 🔌 API Endpoints

### Upload PDF
POST /api/upload

### Chat
POST /api/chat

Request:
{
"sessionId": "user1",
"message": "What are his skills?"
}

Response:
{
"response": "Skills:\n- Java\n- Spring Boot\n- Kafka"
}

<img width="1289" height="851" alt="image" src="https://github.com/user-attachments/assets/9400275e-8e50-4cb7-b80a-04f46907e0fc" />


---

## 🧩 Architecture

PDF → Split → Embed → Qdrant → Retrieve → LLM → Answer

---

## 👨‍💻 Author

Subramanyam D
