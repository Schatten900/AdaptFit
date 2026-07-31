# TODO — AdaptFit

Este documento descreve as tarefas necessárias para implementar o projeto de forma **faseada**.

---

## FASE 1 — Fundação do Projeto (Concluida)

### Backend
- [x] Criar projeto no Spring Initializr
    - Java 17
    - Spring Web, Security, Data JPA, Validation
    - MySQL Driver (Flyway para migrações)
- [x] Configurar `application.properties`
- [x] Configurar Docker + MySQL + Qdrant + Ollama
- [x] Criar `.env`

---

## FASE 2 — Autenticação e Usuário (Concluida)

- [x] Criar entidade `User`
- [x] Criar entidade `Profile`
- [x] Implementar JWT
- [x] Endpoint de cadastro e login
- [x] Proteção de rotas
- [x] CRUD de perfil físico
- [x] Validações (idade, peso, altura)

---

## FASE 3 — Treinos (Core do Produto) (Concluida)

- [x] Criar entidades: WorkoutDay, WorkoutExercise, ExerciseCatalog, WorkoutSession, WorkoutSessionExercise
- [x] Criar treinos manuais (WorkoutDays + WorkoutExercises)
- [x] Registrar treino realizado (WorkoutSession)
- [x] Histórico e dashboard de treinos
- [x] Splits semanais (Split, SplitWorkoutDay)

---

## FASE 4 — Feedback (Concluido)

- [x] Tela de feedback pós-treino
- [x] Nível de fadiga (1-10)
- [x] Dor muscular (1-10)
- [x] Observações livres
- [x] Histórico de feedbacks

---

## FASE 5 — Histórico (Concluida)

- [x] Página com histórico de treinos realizados
- [x] Dashboard com evolução do usuário
- [x] Visualização de sessões
- [x] Filtros por semana, mês e ano
- [x] Filtros por grupo muscular ou exercício
- [x] Gráfico de pizza (distribuição) e gráfico de evolução

---

## FASE 6 — Motor Determinístico (Concluida)

### 6.1 Motor Metabólico (Nutrição)
- [x] Implementar cálculo de TMB (Mifflin-St Jeor)
- [x] Implementar cálculo de TDEE
- [x] Implementar ajuste por objetivo (cutting/bulking/manutenção)
- [x] Implementar distribuição de macros (proteína, gordura, carboidrato)

### 6.2 Motor de Treino
- [x] Implementar escolha de divisão por nível (Full Body, Upper/Lower, PPL, Bro Split)
- [x] Implementar progressão de carga (+2.5%/-5%)
- [x] Implementar detecção de estagnação (3 semanas)
- [x] Implementar deload automático (-35% volume)
- [x] Implementar mudança de estímulo

### 6.3 Estrutura de Dados
- [x] Criar entidades: NutritionalPlan, WorkoutPlan, EvolutionLog
- [x] Implementar repositories

---

## FASE 7 — RAG (Retrieval Augmented Generation) (Concluida)

### 7.1 Banco de Receitas
- [x] Schema de receitas com ingredientes, macros, dietas, alergênios
- [x] Endpoints de busca filtrada
- [x] Configurar Qdrant para embeddings de receitas
- [x] Seed automático via `RecipeDataSeeder`

### 7.2 Banco de Exercícios
- [x] Schema de exercícios com músculos, nível, equipamento
- [x] Popular banco com ~200 exercícios via JSON
- [x] Configurar Qdrant para embeddings de exercícios
- [x] Seed automático via `DataSeeder`

### 7.3 RAG Integration
- [x] Serviço de busca semântica (Qdrant + Ollama embeddings)
- [x] Filtros (objetivo, preferência, alergênios)
- [x] Fallback para busca por similaridade no Qdrant
- [x] Sincronização automática via `@PostConstruct`

---

## FASE 8 — Sistema Multiagente (Pendente)

> Consulte `docs/Agentes.md` para a arquitetura detalhada desta fase.

### 8.1 Infraestrutura da LLM

- [x] Configurar Docker do Ollama
- [x] Implementar `EmbeddingService`
- [ ] Implementar `LlmClient`
    - Comunicação com Ollama
    - Configuração de modelo
    - Temperatura
    - Timeout
    - Tratamento de erros
- [ ] Criar configuração centralizada dos modelos (`LlmProperties`)

---

### 8.2 Agentes

Implementar os agentes especialistas responsáveis por interpretar e organizar as informações produzidas pelo backend.

- [ ] Implementar `TrainingAgent`
- [ ] Implementar `NutritionAgent`
- [ ] Implementar `AgentService` para orquestração dos agentes

---

### 8.3 Context Builders

Implementar os responsáveis por reunir todas as informações determinísticas antes da chamada ao modelo.

#### TrainingContextBuilder

- [ ] Buscar `UserProfile`
- [ ] Buscar `UserPreferences`
- [ ] Buscar `WorkoutPlan`
- [ ] Buscar histórico de treinos
- [ ] Buscar feedbacks
- [ ] Buscar `EvolutionLogs`
- [ ] Consultar `ExerciseRetriever`
- [ ] Construir `TrainingContext`

#### NutritionContextBuilder

- [ ] Buscar `UserProfile`
- [ ] Buscar `UserPreferences`
- [ ] Buscar `NutritionalPlan`
- [ ] Buscar objetivo e macros
- [ ] Consultar `RecipeRetriever`
- [ ] Construir `NutritionContext`

---

### 8.4 RAG Retrievers

Implementar a camada responsável por recuperar documentos relevantes do Qdrant.

- [ ] Implementar `ExerciseRetriever`
- [ ] Implementar `RecipeRetriever`
- [ ] Definir filtros semânticos
- [ ] Implementar fallback para busca vetorial

---

### 8.5 Prompt Builders

Implementar os responsáveis por transformar o contexto em prompts para o modelo.

- [ ] Criar `TrainingPromptBuilder`
- [ ] Criar `NutritionPromptBuilder`

Cada PromptBuilder deverá gerar:

- [ ] System Prompt
- [ ] Contexto do usuário
- [ ] Documentos recuperados pelo RAG
- [ ] Pergunta do usuário
- [ ] Instruções de resposta

---

### 8.6 Persistência das decisões

Registrar todas as interações realizadas pelos agentes.

- [ ] Implementar `AiDecisionLogService`
- [ ] Salvar contexto enviado
- [ ] Salvar documentos recuperados
- [ ] Salvar prompt final
- [ ] Salvar resposta
- [ ] Salvar modelo utilizado
- [ ] Salvar temperatura
- [ ] Salvar latência
- [ ] Salvar tokens de entrada e saída

---

### 8.7 Endpoints

- [ ] Criar `/ai/training/chat`
- [ ] Criar `/ai/nutrition/chat`
- [ ] Validar autenticação
- [ ] Integrar agentes ao frontend

## FASE 9 — Assinaturas e Monetização (Pendente)

- [ ] Planos Free / Premium (PlanType)
- [ ] Limites de uso
- [ ] Bloqueio de features
- [ ] Integração com gateway (futuro)

---

## FASE 10 — Automação e Engajamento (Pendente)

### Scheduler
- [ ] Revisão semanal automática (WeeklyReviewScheduler)
- [ ] Detecção de overtraining (FatigueCheckScheduler)

### Notification
- [ ] Push notifications (PushNotificationProvider)
- [ ] Mensagens motivacionais

---

## FASE 11 — Qualidade e Produção (Pendente)

- [ ] Testes unitários
- [ ] Testes de integração
- [ ] OpenAPI / Swagger
- [ ] Logs estruturados
- [ ] Preparação para deploy

---

## FASE 12 — Fine-tuning (Futuro - Pós-MVP)

### Coletar Dados
- [ ] Registrar interações reais dos usuários (AiDecisionLog)
- [ ] Criar dataset de feedback
- [ ] Avaliar qualidade das respostas

### Fine-tuning
- [ ] Avaliar necessidade real de fine-tuning
- [ ] Se necessário: preparar dataset de treino
- [ ] Fine-tuning de modelo pequeno (7B parâmetros)

---

## Princípios do Sistema

> **Determinístico decide** → Backend calcula TMB, macros, divisão, progressão
>
> **RAG valida** → Banco de dados fornece exercícios e receitas reais
>
> **LLM organiza** → Model organiza e explica, não inventa dados

---

## Observação Final

> Nunca avance uma fase sem ter a anterior **rodando em produção local**.
