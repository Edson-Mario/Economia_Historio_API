# Economia API

API REST para plataforma de conteúdo sobre economia e história de Angola. Permite gestão de artigos, fóruns de discussão, quizzes e utilizadores com diferentes níveis de permissão.

## Stack Tecnológica

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Data JPA** (Hibernate)
- **Spring Validation**
- **Spring Web MVC**
- **MySQL**
- **Lombok**

## Estrutura de Pacotes

```
com.api.ecnomia_api
├── enums/
├── entity/
├── repository/
├── dto/
├── service/
├── controller/
├── exception/
├── config/
```

## Entidades e Relacionamentos

### User (Utilizador)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| nome | String | Nome do utilizador |
| email | String (unique) | E-mail do utilizador |
| senha | String | Senha (hash) |
| tipos | Set\<UserType\> | Tipos do utilizador (pode ter múltiplos) |
| dataCriacao | LocalDateTime | Data de registo |
| ativo | boolean | Se a conta está ativa |

### UserType (Enum)
`USER`, `ADMIN`, `ESCRITOR`, `REVISOR`, `SUPER_ADMIN`

### Article (Artigo)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| titulo | String | Título do artigo |
| texto | @Lob Text | Conteúdo do artigo |
| duracaoLeitura | int | Duração estimada em minutos |
| imagem | String | URL da imagem |
| video | String | URL do vídeo |
| visualizacoes | int | Contador de visualizações |
| dataPublicacao | LocalDateTime | Data de publicação |
| status | ArticleStatus | Pendente, Aprovado, Rejeitado |
| jindungo | boolean | Se é conteúdo restrito |
| categoria | ManyToOne → Category | Categoria do artigo |
| publicador | ManyToOne → User | Quem publicou |
| comentarios | OneToMany → Comment | Comentários do artigo |

### Category (Categoria)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| nome | String (unique) | Nome da categoria |

### Comment (Comentário)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| texto | String | Conteúdo do comentário |
| likes | int | Número de likes |
| denunciado | boolean | Se foi denunciado |
| oculto | boolean | Se está oculto pelo admin |
| dataCriacao | LocalDateTime | Data do comentário |
| usuario | ManyToOne → User | Autor do comentário |
| artigo | ManyToOne → Article (nullable) | Artigo associado |
| forum | ManyToOne → Forum (nullable) | Fórum associado |
| denuncias | OneToMany → Report | Denúncias recebidas |

### Forum (Fórum)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| titulo | String | Título do fórum |
| descricao | String | Descrição do fórum |
| acesso | ForumAccess | PÚBLICO ou PRIVADO |
| criador | ManyToOne → User | Quem criou o fórum |
| categoria | ManyToOne → Category | Categoria do fórum |
| comentarios | OneToMany → Comment | Comentários do fórum |
| solicitacoes | OneToMany → ForumParticipationRequest | Solicitações de participação |

### ForumParticipationRequest
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| status | RequestStatus | PENDENTE, APROVADO, RECUSADO |
| usuario | ManyToOne → User | Utilizador que solicitou |
| forum | ManyToOne → Forum | Fórum solicitado |

### Notification (Notificação)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| tipo | NotificationType | DENUNCIA, NOVO_ARTIGO, ARTIGO_APROVADO, SOLICITACAO_FORUM |
| mensagem | String | Texto da notificação |
| lida | boolean | Se já foi lida |
| usuario | ManyToOne → User | Utilizador alvo |

### Report (Denúncia)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| motivo | String | Motivo da denúncia |
| dataCriacao | LocalDateTime | Data da denúncia |
| status | ReportStatus | PENDENTE, OCULTADO, RESOLVIDO |
| denunciante | ManyToOne → User | Quem denunciou |
| comentario | ManyToOne → Comment | Comentário denunciado |

### Quiz
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| titulo | String | Título do quiz |
| descricao | String | Descrição do quiz |
| publicado | boolean | Se está publicado |
| criador | ManyToOne → User | Admin que criou |
| perguntas | OneToMany → Question | Perguntas do quiz |

### Question (Pergunta)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| enunciado | String | Texto da pergunta |
| pontuacao | int | Pontos da pergunta |
| quiz | ManyToOne → Quiz | Quiz associado |
| alternativas | OneToMany → Alternative | Alternativas da pergunta |

### Alternative (Alternativa)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| texto | String | Texto da alternativa |
| correta | boolean | Se é a resposta correta |
| pergunta | ManyToOne → Question | Pergunta associada |

### QuizAttempt (Tentativa de Quiz)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| pontuacao | int | Pontuação total obtida |
| dataRealizacao | LocalDateTime | Quando foi realizado |
| usuario | ManyToOne → User | Utilizador que respondeu |
| quiz | ManyToOne → Quiz | Quiz respondido |

### UserAnswer (Resposta do Utilizador)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| attempt | ManyToOne → QuizAttempt | Tentativa associada |
| pergunta | ManyToOne → Question | Pergunta respondida |
| alternativa | ManyToOne → Alternative | Alternativa escolhida |

## Endpoints da API

### Autenticação

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/register` | Registar novo utilizador |
| POST | `/api/auth/login` | Login (retorna dados do utilizador) |

### Utilizadores

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| GET | `/api/usuarios` | ADMIN | Listar todos os utilizadores |
| GET | `/api/usuarios/{id}` | USER+ | Ver perfil do utilizador |
| PUT | `/api/usuarios/{id}` | USER+ | Editar própria conta |
| DELETE | `/api/usuarios/{id}` | ADMIN | Remover conta |
| PUT | `/api/usuarios/{id}/tipos` | ADMIN | Gerir tipos do utilizador |

### Categorias

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| GET | `/api/categorias` | Público | Listar todas as categorias |
| POST | `/api/categorias` | ADMIN | Criar nova categoria |
| DELETE | `/api/categorias/{id}` | ADMIN | Remover categoria |

### Artigos

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| GET | `/api/artigos` | Público | Listar artigos aprovados |
| GET | `/api/artigos/categoria/{categoriaId}` | Público | Listar por categoria |
| GET | `/api/artigos/pendentes` | ADMIN | Listar artigos pendentes |
| GET | `/api/artigos/{id}` | Público | Ver artigo (conta visualização) |
| POST | `/api/artigos` | ADMIN / ESCRITOR | Criar artigo (escritor → pendente) |
| PUT | `/api/artigos/{id}` | ADMIN | Editar artigo |
| DELETE | `/api/artigos/{id}` | ADMIN | Remover artigo |
| PUT | `/api/artigos/{id}/aprovar` | ADMIN | Aprovar ou rejeitar artigo |
| PUT | `/api/artigos/{id}/jindungo` | ADMIN | Marcar/desmarcar como jindungo |

### Comentários

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| GET | `/api/artigos/{artigoId}/comentarios` | Público | Comentários do artigo |
| GET | `/api/foruns/{forumId}/comentarios` | Público | Comentários do fórum |
| POST | `/api/artigos/{artigoId}/comentarios` | USER+ | Adicionar comentário ao artigo |
| POST | `/api/foruns/{forumId}/comentarios` | USER+ | Adicionar comentário ao fórum (precisa permissão se privado) |
| PUT | `/api/comentarios/{id}/like` | USER+ | Dar/remover like |
| DELETE | `/api/comentarios/{id}` | USER+ / ADMIN | Remover comentário |

### Fóruns

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| GET | `/api/foruns` | Público | Listar fóruns públicos |
| POST | `/api/foruns` | USER+ | Criar novo fórum |
| GET | `/api/foruns/{id}` | Público | Ver detalhes do fórum |
| POST | `/api/foruns/{id}/solicitar` | USER+ | Solicitar entrada em fórum privado |
| GET | `/api/foruns/solicitacoes` | ADMIN | Listar solicitações pendentes |
| PUT | `/api/foruns/solicitacoes/{id}` | ADMIN | Aprovar/recusar solicitação |

### Denúncias

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| POST | `/api/denuncias` | USER+ | Denunciar um comentário |
| GET | `/api/denuncias` | ADMIN | Listar denúncias pendentes |

### Notificações

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| GET | `/api/notificacoes` | USER+ | Listar notificações do utilizador |
| PUT | `/api/notificacoes/{id}/ler` | USER+ | Marcar notificação como lida |

### Quiz

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| GET | `/api/quiz` | Público | Listar quizzes publicados |
| POST | `/api/quiz` | ADMIN | Criar quiz |
| GET | `/api/quiz/{id}` | Público | Ver quiz (sem alternativas corretas) |
| PUT | `/api/quiz/{id}/publicar` | ADMIN | Publicar quiz |
| POST | `/api/quiz/{id}/perguntas` | ADMIN | Adicionar pergunta ao quiz |
| DELETE | `/api/quiz/perguntas/{id}` | ADMIN | Remover pergunta |
| POST | `/api/quiz/perguntas/{id}/alternativas` | ADMIN | Adicionar alternativa à pergunta |
| DELETE | `/api/quiz/alternativas/{id}` | ADMIN | Remover alternativa |
| POST | `/api/quiz/{id}/responder` | USER+ | Submeter respostas do quiz |
| GET | `/api/quiz/{id}/ranking` | Público | Ver ranking do quiz |

## Regras de Negócio

1. **Tipos de Utilizador**: um utilizador pode ter múltiplos tipos simultaneamente (ex: ADMIN + ESCRITOR + REVISOR)
2. **Artigos Pendentes**: escritores criam artigos com status PENDENTE; só após aprovação do admin ficam visíveis
3. **Textos com Jindungo**: artigos marcados com `jindungo=true` exigem solicitação especial do utilizador para visualizar
4. **Fórum Privado**: para comentar num fórum privado, o utilizador precisa solicitar e o admin aprovar
5. **Denúncias**: utilizadores podem denunciar comentários; o admin oculta o comentário (não apaga)
6. **Notificações**: denúncias geram notificações automáticas para admins
7. **Quiz**: apenas utilizadores autenticados podem responder; não autenticados veem apenas o ranking
8. **Conteúdo não autenticado**: utilizadores não autenticados podem explorar artigos e fóruns públicos mas não podem comentar, criar fóruns ou responder a quizzes

## Como Executar

1. **Pré-requisitos**: Java 17+, MySQL
2. **Configurar banco de dados** em `src/main/resources/application.yml`:
   ```yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/economia_db?createDatabaseIfNotExists=true&useSSL=false
       username: root
       password: sua_senha
     jpa:
       hibernate.ddl-auto: update
       properties:
         hibernate:
           dialect: org.hibernate.dialect.MySQLDialect
   ```
3. **Compilar e executar**:
   ```bash
   ./mvnw spring-boot:run
   ```
4. A API estará disponível em `http://localhost:8080/api`
