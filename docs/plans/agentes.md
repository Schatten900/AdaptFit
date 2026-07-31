# Agentes.md

# Arquitetura dos Agentes de IA

O AdaptFit utiliza uma arquitetura baseada em **LLM + RAG**, porém **todas as decisões críticas permanecem determinísticas**.

A Inteligência Artificial **não substitui** as regras de negócio implementadas no backend.

Ela atua apenas como uma camada de interpretação e personalização das informações calculadas pelo sistema.

---

# Filosofia

Os agentes **não calculam**.

Os agentes **não decidem regras de negócio**.

Os agentes apenas:

- organizam informações produzidas pelo backend;
- explicam decisões determinísticas;
- respondem dúvidas do usuário;
- personalizam a comunicação;
- utilizam RAG para enriquecer respostas;
- sugerem alternativas compatíveis com o contexto do usuário.

Toda informação enviada ao modelo já passou pelas regras do sistema.

---

# Arquitetura Geral

```
Usuário
    │
    ▼
Controller
    │
    ▼
AgentService
    │
 ┌──┴───────────────┐
 │                  │
 ▼                  ▼
TrainingAgent   NutritionAgent
 │                  │
 ▼                  ▼
ContextBuilder  ContextBuilder
 │                  │
 ▼                  ▼
RAG Retriever   RAG Retriever
 │                  │
 ▼                  ▼
PromptBuilder   PromptBuilder
 │                  │
 └──────┬───────────┘
        ▼
    LlmClient
        │
        ▼
      Ollama
        │
        ▼
Resposta
        │
        ▼
AiDecisionLog
```

---

# Responsabilidade de cada camada

## Controller

Responsável apenas por receber a requisição HTTP.

Não possui nenhuma lógica de IA.

Exemplo:

```
POST /ai/training/chat

POST /ai/nutrition/chat
```

---

## AgentService

Responsável por decidir qual agente deverá atender a requisição.

Exemplo

```
Treino

↓

TrainingAgent
```

```
Nutrição

↓

NutritionAgent
```

Não monta prompts.

Não consulta banco.

---

# TrainingAgent

Especialista em treinamento.

Nunca consulta diretamente o banco.

Recebe um contexto completamente preparado.

Responsabilidades:

- explicar exercícios
- responder dúvidas
- interpretar feedbacks
- justificar progressões
- adaptar exercícios
- sugerir substituições
- explicar execução correta
- explicar divisão do treino

---

# NutritionAgent

Especialista em nutrição.

Nunca consulta diretamente o banco.

Responsabilidades:

- gerar sugestões alimentares
- explicar distribuição de macros
- adaptar refeições
- substituir alimentos
- responder dúvidas nutricionais
- explicar escolhas alimentares

---

# ContextBuilder

O ContextBuilder é responsável por reunir todas as informações necessárias para responder ao usuário.

Ele funciona como uma camada entre o banco de dados e o modelo.

O LLM nunca acessa diretamente o banco.

Fluxo:

```
TrainingContextBuilder

↓

UserProfile

↓

UserPreferences

↓

WorkoutPlan

↓

WorkoutHistory

↓

WorkoutFeedback

↓

EvolutionLogs

↓

ExerciseRetriever (RAG)

↓

TrainingContext
```

O resultado é um único objeto contendo todas as informações necessárias.

Exemplo:

```java
TrainingContext {

    UserProfile profile;

    UserPreferences preferences;

    WorkoutPlan currentPlan;

    List<WorkoutSession> history;

    List<Feedback> feedbacks;

    List<EvolutionLog> evolution;

    List<ExerciseDocument> ragDocuments;

    String userQuestion;

}
```

O mesmo conceito vale para:

```
NutritionContextBuilder
```

---

# RAG Retriever

O RAG é responsável apenas por recuperar conhecimento.

Ele nunca conversa com o usuário.

Ele nunca interpreta informações.

Ele apenas retorna documentos relevantes.

## ExerciseRetriever

Fonte:

Exercise RAG

Recebe:

```
"Quero trocar o supino."
```

Retorna:

```
Supino Máquina

Chest Press

Flexão

Crucifixo Máquina
```

---

## RecipeRetriever

Fonte:

Recipe RAG

Recebe:

```
Preciso de uma refeição rica em proteína.
```

Retorna receitas semelhantes.

---

# PromptBuilder

Depois que o contexto está completo, o PromptBuilder transforma todas essas informações em um prompt para o modelo.

O PromptBuilder não consulta banco.

O PromptBuilder não executa RAG.

Sua única responsabilidade é estruturar o prompt.

Cada agente possui seu próprio PromptBuilder.

```
TrainingPromptBuilder

NutritionPromptBuilder
```

---

## Estrutura do Prompt

Todo prompt possui quatro partes.

### 1. System Prompt

Define quem é o agente.

Exemplo:

```
Você é um treinador especializado em musculação.

Nunca invente exercícios.

Nunca faça cálculos de macros.

Utilize apenas as informações fornecidas.

Caso não saiba responder, informe que não possui informações suficientes.
```

---

### 2. Contexto

Informações do usuário.

Exemplo:

```
Idade

Peso

Objetivo

Experiência

Lesões

Equipamentos

Treino atual

Feedbacks

Histórico

Exercícios recuperados pelo RAG
```

---

### 3. Pergunta

Pergunta realizada pelo usuário.

Exemplo:

```
Meu cotovelo dói durante o supino.

Existe outro exercício?
```

---

### 4. Instruções de resposta

Define o formato esperado.

Exemplo:

```
Responda em português.

Explique o motivo da substituição.

Utilize apenas exercícios presentes no contexto.

Não invente alimentos.

Não invente exercícios.
```

---

# LlmClient

Camada responsável pela comunicação com o modelo.

Responsabilidades:

- enviar prompts
- configurar temperatura
- configurar modelo
- controlar timeout
- tratar erros
- receber resposta

O restante da aplicação nunca conversa diretamente com o Ollama.

Sempre utiliza o LlmClient.

---

# Ollama

O Ollama executa o modelo local.

Exemplo:

```
llama3

qwen3

mistral

gemma
```

A troca de modelo não altera nenhuma outra camada da aplicação.

---

# Fluxo completo

```
Usuário

↓

TrainingAgent

↓

TrainingContextBuilder

↓

Busca Profile

↓

Busca Preferences

↓

Busca Workout

↓

Busca Feedbacks

↓

Consulta Exercise RAG

↓

TrainingPromptBuilder

↓

LlmClient

↓

Ollama

↓

Resposta

↓

AiDecisionLog
```

---

# Persistência das respostas

Toda interação com a IA deve ser registrada.

Objetivos:

- auditoria;
- debugging;
- avaliação da qualidade das respostas;
- futura criação de datasets;
- suporte ao fine-tuning.

Cada interação gera um registro em:

```
AiDecisionLog
```

---

# Princípios da arquitetura

## Separação de responsabilidades

Cada camada possui apenas uma responsabilidade.

Controller

↓

Agent

↓

ContextBuilder

↓

Retriever

↓

PromptBuilder

↓

LlmClient

↓

Ollama

---

## Determinismo

Toda decisão crítica pertence ao backend.

Exemplos:

- cálculo de TMB;
- cálculo de TDEE;
- distribuição de macros;
- progressão de carga;
- deload;
- divisão de treino;
- seleção inicial de exercícios;
- validação de restrições.

A IA nunca substitui essas regras.

---

## RAG

O modelo nunca responde utilizando apenas conhecimento próprio.

Sempre que possível utiliza documentos recuperados pelo RAG.

Isso reduz alucinações e mantém as respostas alinhadas ao banco de exercícios e receitas do AdaptFit.

---
