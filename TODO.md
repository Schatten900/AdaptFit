# TODO — AdaptFit 🧠💪

Este documento descreve as tarefas necessárias para implementar o projeto CoachAI de forma **faseada**, priorizando um MVP funcional e evoluindo até uma solução completa com IA.

---

## 🟢 FASE 1 — Fundação do Projeto (Concluida)

### Backend
- [ ] Criar projeto no Spring Initializr
    - Java 21
    - Spring Web
    - Spring Security
    - Spring Data JPA
    - Validation
    - PostgreSQL Driver
- [ ] Configurar `application.yml`
- [ ] Configurar Docker + MySQL
- [ ] Criar `.env.example`

---

## 🟢 FASE 2 — Autenticação e Usuário (Concluida)

### Auth
- [ ] Criar entidade `User`
- [ ] Criar entidade `UserProfile`
- [ ] Implementar JWT
- [ ] Endpoint de cadastro
- [ ] Endpoint de login
- [ ] Proteção de rotas

### User
- [ ] CRUD de perfil físico
- [ ] Validações (idade, peso, altura)

---

## 🟢 FASE 3 — Treinos (Core do Produto) (Concluida)

### Workout
- [ ] Criar entidades:
    - Workout
    - WorkoutDay
    - Exercise
    - WorkoutSession
- [ ] Criar treinos manuais
- [ ] Registrar treino realizado
- [ ] Histórico de treinos

---

## 🟡 FASE 4 — Feedback e Progresso (Pendente)

### Feedback (gerados pela IA, por meio da visualização e calculo de volume/fadiga/dor muscular/historico do usuario)
- [ ] Pagina de feedback
- [ ] Feedback pós-treino (incluso ja no endworkout)
- [ ] Nível de fadiga   
- [ ] Dor muscular
- [ ] Observações livres


## 🟡 FASE 5 — Feedback e Progresso (Pendente)

### Historico
- [] Pagina com historico de treinos realizados pelo usuario
- [] Dashboard presente na pagina de historico para evolução do usuario
- [] Visualização de sessions
- [] Filtragem para visualizar historico em semana, mes e ano.
- [] Filtragem para visualizar dashboard por exercicio ou grupo muscular (por padrão, mostra todos os grupos musculares)
- [] Tipo de dashboard a ser utilizado como grafico de pizza e grafico plano.

---

## 🟠 FASE 6 — Inteligência Artificial (Inicial) (Pendente)

### IA Base
- [ ] Criar `AIOrchestrator`
- [ ] Criar interface `AIAgent`
- [ ] Implementar:
    - CoachAgent
    - FatigueAgent
    - ProgressionAgent
- [ ] Prompt Builder
- [ ] Registro de decisões da IA

---

## 🟠 FASE 7 — IA Avançada (LLM) (Pendente)

- [ ] Integração com LLM externo
- [ ] Contexto histórico do usuário
- [ ] Ajuste automático de treinos
- [ ] Restrições de segurança

---

## 🔵 FASE 8 — Assinaturas e Monetização (Pendente)

### Subscription
- [ ] Planos Free / Premium
- [ ] Limites de uso
- [ ] Bloqueio de features
- [ ] Integração com gateway (futuro)

---

## 🔵 FASE 9 — Automação e Engajamento (Pendente)

### Scheduler
- [ ] Revisão semanal automática
- [ ] Detecção de overtraining

### Notification
- [ ] Push notifications
- [ ] Mensagens motivacionais

---

## 🟣 FASE 10 — Qualidade e Produção (Pendente)

- [ ] Testes unitários
- [ ] Testes de integração
- [ ] OpenAPI / Swagger
- [ ] Logs estruturados
- [ ] Preparação para deploy

---

## 📌 Observação Final 

> Nunca avance uma fase sem ter a anterior **rodando em produção local**.


