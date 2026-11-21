# API Inteligente de Orçamento com Spring Boot e Spring AI

## O que o projeto faz
API de orçamento financeiro que processa comandos de voz: recebe um áudio, transcreve em texto, usa IA (Tool Calling) para executar funções reais da aplicação (criar e consultar transações) e devolve a resposta em áudio.

Fluxo: áudio → transcrição → `ChatClient` escolhe a tool → use case → banco → resposta em voz.

## Melhoria implementada
Nova tool de consulta: **soma das transações por categoria** ("quanto gastei em mercado?").

- `SumTransactionsByCategoryUseCase` (camada `application`): usa o `TransactionRepository` existente e soma os valores da categoria. A tool é exposta com `@Tool(name = "sum-transactions-by-category")`.
- `CategoryTotalOutput`: devolve o total em centavos e em reais.
- Registro no `TransactionController`, em `defaultTools(...)`.
- Testes unitários com Mockito (categoria com transações e categoria vazia).

Decisão de projeto: a ideia inicial era "gasto do mês", mas `Transaction` não tem data. Mudar isso exigiria alterar a entidade e o banco, então escolhi a soma por categoria, que reaproveita o modelo atual.

## Tecnologias
Java 25, Spring Boot 4, Spring AI, Spring Data JPA, MySQL (Docker Compose), Gradle, Lombok, JUnit 5 e Mockito, OpenAI (transcrição, chat e voz).

## Como executar
Pré-requisitos: JDK 25, Docker Desktop e uma chave da OpenAI.

```powershell
cd 05-spring-ai
$env:OPENAI_API_KEY="sua_chave"
.\gradlew bootRun
```

## Como testar
Testes unitários da melhoria:
```powershell
.\gradlew test --tests "*SumTransactionsByCategoryUseCaseTest"
```

Fluxo completo (precisa de crédito na API da OpenAI):
1. Cadastrar transações: `POST /transactions` com `{"description":"Arroz","amount":1500,"category":"GROCERIES"}` (valor em centavos).
2. Enviar um áudio perguntando "Quanto eu gastei em mercado?" para `POST /transactions/ai` (campo `file`, multipart). A resposta é um MP3.

## Limitações
- O teste de ponta a ponta com áudio **não foi concluído**: a conta da OpenAI ficou sem crédito e a transcrição retornou `insufficient_quota`. A lógica foi validada pelos testes unitários.
- Observação: `TransactionOutput` não converte centavos para reais (o `setScale(2)` sobre um `long` não divide por 100). Não alterei para manter o escopo pequeno.

## O que aprendi
(escreva aqui com suas palavras)
- Como o Tool Calling funciona: o `@Tool` expõe um use case para a IA, e a descrição é o que ela lê para decidir quando chamar.
- A organização em camadas: a IA chama o mesmo use case que o REST usaria.
- Dificuldades reais: arquivos criados sem a extensão `.java`, alterações não salvas, campo `final` atribuído duas vezes, a toolchain do Java 25, e ler o log para descobrir que o erro 500 vinha da falta de crédito na API.