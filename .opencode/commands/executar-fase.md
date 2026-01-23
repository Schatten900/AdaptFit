# Execução de Projeto de IA por Fluxo Estrito de Fases

Executar o projeto de IA utilizando um **fluxo estrito por fases**, com **governança**, **revisão obrigatória** e **execução segura**.

## Comportamento do OpenCode

O OpenCode deve:

- Ler o arquivo `TODO.md`;
- Identificar a **primeira fase** com status **“Pendente”**;
- Executar **apenas essa fase**;
- **Nunca** executar mais de uma fase por invocação.

---

## Fontes Autoritativas

- **Regras do projeto:** `@README.md`
- **Plano de implementação:** `@docs/plans/IA.md`
- **Estado do projeto:** `@TODO.md`
- **Código de referência:** repositório atual (**somente leitura**)

---

## Regras Obrigatórias

### Controle de Fase

- Ler `TODO.md`;
- Selecionar a fase mais inicial com status **“Pendente”**;
- Executar **somente essa fase**;
- Se não houver fases pendentes, **interromper imediatamente**.

---

### Análise Eficiente (Tokens)

- Não ingerir o repositório inteiro;
- Descobrir padrões focando no próprio repositório;
- Abrir somente trechos relevantes e comparar estruturas;
- Evitar copiar/colar código do core.

---

### Implementação da Fase

Para a fase selecionada:

- Implementar **logs**, **validação** e **tratamento de erros** conforme o plano;
- Adicionar **testes mínimos**, porém representativos.

---

### Revisão de Código (Obrigatória)

Após concluir a implementação **e antes de qualquer commit**:

### Executar o agente de revisão

Executar o agente `code-review` utilizando o prompt padrão:

Execute o agente code-review utilizando o prompt:
.opencode/code-review.prompt.md

Revise apenas os arquivos modificados nesta fase.
Compare com README.md e TODO.md.

### Pré-Commit (Obrigatório)

O OpenCode deve **parar** e apresentar:

- Lista de arquivos **criados / modificados / deletados**;
- Motivação de cada mudança;
- Resumo dos diffs (principais alterações por arquivo);
- Declaração explícita de que a **fase foi concluída**.

O OpenCode deve **aguardar aprovação explícita** antes de:

- Comitar;
- Sugerir push;
- Executar comandos de manutenção.

---

### Atualização do TODO

Somente após aprovação:

- Atualizar `TODO.md`, marcando a fase como **“Concluída”**;
- Realizar **commit separado** para essa atualização  
  (exemplo: `chore(todo): Fase X concluída`).

---

### Relatório de Conclusão

Ao final da execução:

- Informar a **fase executada**;
- Resumir os **resultados**;
- Indicar **limitações conhecidas** (se houver);
- Apontar a **próxima fase pendente** (sem executar).

---

### Execução em Terminal

- Trabalhar no **workspace atual**;
- O IDE (IntelliJ / VS Code) **não influencia** o fluxo via CLI;
- Antes de rodar qualquer comando de shell:
  - Mostrar o comando;
  - Explicar o impacto;
  - Aguardar autorização;
- Em alterações amplas, preferir **commits pequenos e descritivos**.
