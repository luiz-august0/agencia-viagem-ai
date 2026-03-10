# 🌍 Agência de Viagem AI

Sistema inteligente de agência de viagens desenvolvido com IA generativa, utilizando arquitetura de agentes e RAG (Retrieval-Augmented Generation) para fornecer assistência personalizada aos clientes.

## 📋 Visão Geral

Este projeto implementa uma solução completa de atendimento ao cliente para agências de viagem, composta por dois microserviços principais que trabalham em conjunto:

- **Agent**: Serviço principal com agentes de IA especializados em atendimento ao cliente
- **MCP Booking Service**: Serviço de gerenciamento de reservas utilizando o protocolo MCP (Model Context Protocol)

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada em microserviços com comunicação via MCP:

```
┌─────────────────────────────────────┐
│         Agent Service               │
│  (Porta 8080)                       │
│                                     │
│  ┌──────────────────────────────┐  │
│  │  TravelAgentAssistant        │  │
│  │  PackageExpert               │  │
│  │  + Guardrails (Segurança)    │  │
│  └──────────────────────────────┘  │
│              │                      │
│              │ MCP Protocol         │
│              ▼                      │
└──────────────┼──────────────────────┘
               │
               │ HTTP/SSE
               │
┌──────────────▼──────────────────────┐
│    MCP Booking Service              │
│    (Porta 8081)                     │
│                                     │
│  ┌──────────────────────────────┐  │
│  │  BookingTools                │  │
│  │  - Consultar reservas        │  │
│  │  - Cancelar reservas         │  │
│  │  - Listar pacotes            │  │
│  └──────────────────────────────┘  │
└─────────────────────────────────────┘
```

## 🚀 Tecnologias Utilizadas

### Framework e Runtime
- **Quarkus 3.31.1**: Framework Java supersônico e nativo em Kubernetes
  - Startup ultrarrápido e baixo consumo de memória
  - Otimizado para GraalVM e HotSpot
  - Live reload para desenvolvimento ágil

### Inteligência Artificial
- **LangChain4j**: Framework para desenvolvimento de aplicações com LLMs
  - Integração com modelos de linguagem
  - Suporte a RAG (Retrieval-Augmented Generation)
  - Sistema de agentes e ferramentas

- **Ollama**: Execução local de modelos de IA
  - Modelo de chat: `llama3.2:latest`
  - Modelo de embeddings: `nomic-embed-text`

### Armazenamento e Vetorização
- **PGVector**: Extensão PostgreSQL para armazenamento de embeddings
  - Busca semântica eficiente
  - Dimensão de vetores: 768
  - Tabela: `travel_embeddings`

### Protocolo MCP
- **Model Context Protocol**: Protocolo para comunicação entre agentes e ferramentas
  - Transporte via HTTP/SSE (Server-Sent Events)
  - Timeout configurável para recursos
  - Logging detalhado de requisições e respostas

### Segurança e Qualidade
- **Guardrails**: Sistema de proteção para entrada e saída de dados
  - `InjectionGuard`: Proteção contra prompt injection
  - `ToneGuard`: Validação de tom e qualidade das respostas
  - Máximo de 3 tentativas de retry

## 📦 Estrutura do Projeto

```
agencia-viagem-ai/
├── agent/                          # Serviço principal de agentes IA
│   ├── src/main/java/agent/dev/ia/
│   │   ├── inbound/http/          # Controllers REST
│   │   │   └── TravelAgentController.java
│   │   ├── infra/config/          # Configurações
│   │   │   ├── ChatMemoryConfig.java
│   │   │   ├── DocumentIngestor.java
│   │   │   └── RagConfiguration.java
│   │   └── outbound/ai/
│   │       ├── agents/            # Agentes especializados
│   │       │   ├── TravelAgentAssistant.java
│   │       │   ├── PackageExpert.java
│   │       │   ├── PackageSecurityExpert.java
│   │       │   └── ToneJudge.java
│   │       └── guardrail/         # Proteções de segurança
│   │           ├── InjectionGuard.java
│   │           └── ToneGuard.java
│   └── src/main/resources/
│       └── application.properties
│
└── mcp-booking-service/           # Serviço de reservas (MCP)
    ├── src/main/java/booking/service/dev/ia/
    │   ├── core/domain/
    │   │   ├── entity/            # Entidades de domínio
    │   │   │   └── Booking.java
    │   │   ├── enums/             # Enumerações
    │   │   │   ├── BookingStatus.java
    │   │   │   └── Category.java
    │   │   └── usecase/           # Casos de uso
    │   │       ├── CancelBookingUC.java
    │   │       ├── GetBookingDetailsUC.java
    │   │       └── GetBookingsByCategoryUC.java
    │   ├── core/gateway/          # Interfaces de repositório
    │   │   └── BookingRepository.java
    │   ├── infra/repository/      # Implementações
    │   │   └── BookingRepositoryMemory.java
    │   └── outbound/tools/        # Ferramentas MCP
    │       └── BookingTools.java
    └── src/main/resources/
        └── application.properties
```

## 🔧 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 25** ou superior
- **Maven 3.8+**
- **Ollama** (para execução dos modelos de IA)
- **PostgreSQL** com extensão PGVector (para armazenamento de embeddings)

### Instalação do Ollama

```bash
# macOS
brew install ollama

# Linux
curl -fsSL https://ollama.com/install.sh | sh

# Baixar os modelos necessários
ollama pull llama3.2:latest
ollama pull nomic-embed-text
```

### Configuração do PostgreSQL com PGVector

```bash
# Instalar PostgreSQL
brew install postgresql@16

# Instalar extensão PGVector
brew install pgvector

# Iniciar PostgreSQL
brew services start postgresql@16

# Criar banco de dados
createdb travel_db

# Conectar e habilitar extensão
psql travel_db
CREATE EXTENSION vector;
```

## 🚀 Como Executar

### 1. Iniciar o Ollama

```bash
# Iniciar o serviço Ollama
ollama serve
```

O Ollama estará disponível em `http://localhost:11434`

### 2. Executar o MCP Booking Service

Em um terminal, navegue até o diretório do serviço de reservas:

```bash
cd mcp-booking-service

# Modo desenvolvimento (com hot reload)
./mvnw quarkus:dev

# Ou compilar e executar
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

O serviço estará disponível em `http://localhost:8081`

### 3. Executar o Agent Service

Em outro terminal, navegue até o diretório do agente:

```bash
cd agent

# Modo desenvolvimento (com hot reload)
./mvnw quarkus:dev

# Ou compilar e executar
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

O serviço estará disponível em `http://localhost:8080`

## 📡 Endpoints da API

### Agent Service (Porta 8080)

#### 1. Chat Geral com Assistente de Viagens
```bash
POST /travel
Content-Type: text/plain

# Exemplo
curl -X POST http://localhost:8080/travel \
  -H "Content-Type: text/plain" \
  -d "Quais são os destinos disponíveis?"
```

#### 2. Consulta Especializada em Pacotes (com autenticação)
```bash
POST /travel/package
Content-Type: text/plain
X-User-Name: <nome-do-usuario>

# Exemplo
curl -X POST http://localhost:8080/travel/package \
  -H "Content-Type: text/plain" \
  -H "X-User-Name: João Silva" \
  -d "Quero cancelar minha reserva número 123"
```

### MCP Booking Service (Porta 8081)

O serviço MCP expõe ferramentas via protocolo MCP que são consumidas automaticamente pelo Agent Service:

- **getBookingDetails**: Obtém detalhes de uma reserva pelo ID
- **cancelBooking**: Cancela uma reserva existente
- **listPackagesByCategory**: Lista pacotes por categoria (ADVENTURE, TREASURES, etc.)

## 🛡️ Funcionalidades de Segurança

### Guardrails Implementados

1. **InjectionGuard** (Input)
   - Protege contra tentativas de prompt injection
   - Valida entrada do usuário antes do processamento
   - Bloqueia comandos maliciosos

2. **ToneGuard** (Output)
   - Valida o tom das respostas geradas
   - Garante respostas profissionais e adequadas
   - Utiliza agente especializado (ToneJudge) para avaliação

### Sistema de Retry
- Máximo de 3 tentativas em caso de falha nos guardrails
- Configurável via `quarkus.langchain4j.guardrails.max-retries`

## 🧠 Agentes Especializados

### TravelAgentAssistant
- Assistente geral para consultas sobre viagens
- Responde perguntas básicas sobre destinos e serviços

### PackageExpert
- Especialista em pacotes de viagem da "Mundo Viagens"
- Utiliza RAG para buscar informações em documentos
- Integrado com BookingTools via MCP
- Protegido por guardrails de entrada e saída
- Mantém histórico de conversação por usuário (Memory)

### PackageSecurityExpert
- Especialista em validação de segurança
- Avalia riscos em operações sensíveis

### ToneJudge
- Avalia a qualidade e tom das respostas
- Garante comunicação profissional

## 📊 Configurações Importantes

### Agent Service (`agent/src/main/resources/application.properties`)

```properties
# Ollama
quarkus.langchain4j.ollama.base-url=http://localhost:11434/
quarkus.langchain4j.ollama.chat-model.model-id=llama3.2:latest
quarkus.langchain4j.ollama.timeout=60s
quarkus.langchain4j.ollama.embedding-model.model-id=nomic-embed-text

# PGVector
quarkus.langchain4j.pgvector.dimension=768
quarkus.langchain4j.pgvector.table=travel_embeddings
quarkus.langchain4j.pgvector.drop-table-first=true

# MCP
quarkus.langchain4j.mcp.booking-server.transport-type=http
quarkus.langchain4j.mcp.booking-server.url=http://localhost:8081/mcp/sse/
quarkus.langchain4j.mcp.booking-server.resources-timeout=30s

# Guardrails
quarkus.langchain4j.guardrails.max-retries=3

# Logging
quarkus.langchain4j.log-requests=true
quarkus.langchain4j.log-responses=true
```

### MCP Booking Service (`mcp-booking-service/src/main/resources/application.properties`)

```properties
# Servidor
quarkus.http.port=8081

# MCP Server Info
quarkus.mcp.server.server-info.name=Booking Tools Service
quarkus.mcp.server.server-info.version=1.0.0

# Logging
quarkus.mcp.server.traffic-logging.enabled=true
quarkus.mcp.server.traffic-logging.text-limit=2000
```

## 🧪 Testes

### Executar testes unitários

```bash
# Agent Service
cd agent
./mvnw test

# MCP Booking Service
cd mcp-booking-service
./mvnw test
```

### Executar testes de integração

```bash
./mvnw verify
```

## 📦 Build para Produção

### Build JVM

```bash
# Agent Service
cd agent
./mvnw clean package

# MCP Booking Service
cd mcp-booking-service
./mvnw clean package
```

### Build Nativo (GraalVM)

```bash
# Requer GraalVM instalado
./mvnw package -Pnative
```

O executável nativo será gerado em `target/*-runner` com:
- Startup em milissegundos
- Consumo mínimo de memória
- Ideal para containers e serverless

## 🐳 Docker

```bash
# Build da imagem JVM
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t agencia-viagem-ai .

# Build da imagem nativa
./mvnw package -Pnative
docker build -f src/main/docker/Dockerfile.native -t agencia-viagem-ai:native .
```

## 🔍 Monitoramento e Logs

Os serviços possuem logging detalhado habilitado:

- Requisições e respostas do LangChain4j
- Tráfego MCP entre serviços
- Execução de guardrails
- Operações de RAG e busca vetorial

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto é privado e proprietário.

## 👥 Autores

Desenvolvido por Luiz Augusto Marques

## 📞 Suporte

Para dúvidas ou problemas, abra uma issue no repositório.

---

**Nota**: Este projeto utiliza modelos de IA executados localmente via Ollama. Certifique-se de ter recursos computacionais adequados (recomendado: 8GB+ RAM, GPU opcional para melhor performance).
