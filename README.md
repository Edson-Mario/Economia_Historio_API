# Economia API

API REST para plataforma de conteúdo sobre economia e história de Angola. Permite gestão de artigos, fóruns de discussão, quizzes e utilizadores com diferentes níveis de permissão.

## Stack Tecnológica

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Data JPA** (Hibernate)
- **Spring Security** (JWT)
- **Spring Validation**
- **Spring Web MVC**
- **MySQL**
- **Lombok**
- **jjwt** (0.12.6)

## Estrutura de Pacotes

```
com.api.ecnomia_api
├── config/          # CorsConfig, AuthConfig
├── controller/      # Controladores REST
├── dto/             # Request/Response DTOs
├── entity/          # Entidades JPA
├── enums/           # Enumeradores
├── exception/       # Exceções e handlers
├── repository/      # Repositórios JPA
├── security/        # JWT, SecurityConfig, UserDetailsServiceImpl
├── service/         # Lógica de negócio
└── EcnomiaApiApplication.java
```

## Entidades e Relacionamentos

### User (Utilizador)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| nome | String | Nome do utilizador |
| email | String (unique) | E-mail do utilizador |
| senha | String | Senha (hash BCrypt) |
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
| paragrafos | OneToMany → Paragraph | Parágrafos do artigo (ordenados) |

### Paragraph (Parágrafo)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| titulo | String | Título do parágrafo |
| texto | @Lob LONGTEXT | Conteúdo do parágrafo |
| artigo | ManyToOne → Article | Artigo associado |

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

### ForumAccess (Enum)
`PUBLICO`, `PRIVADO`

### ForumParticipationRequest
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| status | RequestStatus | PENDENTE, APROVADO, RECUSADO |
| usuario | ManyToOne → User | Utilizador que solicitou |
| forum | ManyToOne → Forum | Fórum solicitado |

### RequestStatus (Enum)
`PENDENTE`, `APROVADO`, `RECUSADO`

### Notification (Notificação)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| tipo | NotificationType | DENUNCIA, NOVO_ARTIGO, ARTIGO_APROVADO, SOLICITACAO_FORUM |
| mensagem | String | Texto da notificação |
| lida | boolean | Se já foi lida |
| usuario | ManyToOne → User | Utilizador alvo |

### NotificationType (Enum)
`DENUNCIA`, `NOVO_ARTIGO`, `ARTIGO_APROVADO`, `SOLICITACAO_FORUM`

### Report (Denúncia)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| motivo | String | Motivo da denúncia |
| dataCriacao | LocalDateTime | Data da denúncia |
| status | ReportStatus | PENDENTE, OCULTADO, RESOLVIDO |
| denunciante | ManyToOne → User | Quem denunciou |
| comentario | ManyToOne → Comment | Comentário denunciado |

### ReportStatus (Enum)
`PENDENTE`, `OCULTADO`, `RESOLVIDO`

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

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| POST | `/api/auth/register` | ❌ Público | Registar novo utilizador |
| POST | `/api/auth/login` | ❌ Público | Login (retorna JWT + dados do utilizador) |

### Utilizadores

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| GET | `/api/usuarios` | ADMIN | Listar todos os utilizadores |
| GET | `/api/usuarios/{id}` | ❌ Público | Ver perfil do utilizador |
| PUT | `/api/usuarios/{id}` | Proprietário ou ADMIN | Editar conta (nome, email, senha) |
| DELETE | `/api/usuarios/{id}` | ADMIN | Desativar conta (soft-delete) |
| PUT | `/api/usuarios/{id}/tipos` | ADMIN | Gerir tipos do utilizador |

### Categorias

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| GET | `/api/categorias` | ❌ Público | Listar todas as categorias |
| POST | `/api/categorias` | ADMIN | Criar nova categoria |
| DELETE | `/api/categorias/{id}` | ADMIN | Remover categoria |

### Artigos

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| GET | `/api/artigos` | ❌ Público | Listar artigos aprovados |
| GET | `/api/artigos/categoria/{categoriaId}` | ❌ Público | Listar por categoria |
| GET | `/api/artigos/pendentes` | ADMIN | Listar artigos pendentes |
| GET | `/api/artigos/{id}` | ❌ Público | Ver artigo (incrementa visualização) |
| POST | `/api/artigos` | USER+ | Criar artigo (não-admin → pendente) |
| PUT | `/api/artigos/{id}` | ADMIN | Editar artigo |
| DELETE | `/api/artigos/{id}` | ADMIN | Remover artigo |
| PUT | `/api/artigos/{id}/aprovar?aprovado=true` | ADMIN | Aprovar ou rejeitar artigo |
| PUT | `/api/artigos/{id}/jindungo` | ADMIN | Marcar/desmarcar como jindungo |
| POST | `/api/artigos/{id}/paragrafos` | ADMIN | Adicionar parágrafo ao artigo |
| PUT | `/api/artigos/{id}/paragrafos/{paragraphId}` | ADMIN | Editar parágrafo |
| DELETE | `/api/artigos/{id}/paragrafos/{paragraphId}` | ADMIN | Remover parágrafo |

### Comentários

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| GET | `/api/artigos/{artigoId}/comentarios` | ❌ Público | Listar comentários do artigo |
| GET | `/api/foruns/{forumId}/comentarios` | ❌ Público | Listar comentários do fórum |
| POST | `/api/artigos/{artigoId}/comentarios` | USER+ | Adicionar comentário ao artigo |
| POST | `/api/foruns/{forumId}/comentarios` | USER+ | Adicionar comentário ao fórum (necessita permissão se privado) |
| PUT | `/api/comentarios/{id}/like` | ❌ Público | Dar like |
| DELETE | `/api/comentarios/{id}` | Proprietário ou ADMIN | Remover comentário |

### Fóruns

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| GET | `/api/foruns` | ❌ Público | Listar fóruns públicos |
| POST | `/api/foruns` | USER+ | Criar novo fórum |
| GET | `/api/foruns/{id}` | ❌ Público | Ver detalhes do fórum |
| POST | `/api/foruns/{id}/solicitar` | USER+ | Solicitar entrada em fórum privado |
| GET | `/api/foruns/solicitacoes` | ADMIN | Listar solicitações pendentes |
| PUT | `/api/foruns/solicitacoes/{id}?aprovado=true` | ADMIN | Aprovar/recusar solicitação |

### Denúncias

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| POST | `/api/denuncias` | USER+ | Denunciar comentário (não pode denunciar o próprio) |
| GET | `/api/denuncias` | ADMIN | Listar denúncias pendentes |
| PUT | `/api/denuncias/{id}/ocultar` | ADMIN | Ocultar comentário denunciado |

### Notificações

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| GET | `/api/notificacoes` | USER+ | Listar notificações do utilizador |
| GET | `/api/notificacoes/nao-lidas` | USER+ | Contar notificações não lidas |
| PUT | `/api/notificacoes/{id}/ler` | USER+ (proprietário) | Marcar notificação como lida |

### Quiz

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| GET | `/api/quiz` | ❌ Público | Listar quizzes publicados |
| POST | `/api/quiz` | ADMIN | Criar quiz (rascunho) |
| GET | `/api/quiz/{id}` | ❌ Público | Ver quiz (admin vê respostas corretas) |
| PUT | `/api/quiz/{id}/publicar` | ADMIN | Publicar quiz |
| POST | `/api/quiz/{id}/perguntas` | ADMIN | Adicionar pergunta ao quiz |
| DELETE | `/api/quiz/perguntas/{id}` | ADMIN | Remover pergunta |
| POST | `/api/quiz/perguntas/{id}/alternativas` | ADMIN | Adicionar alternativa à pergunta |
| DELETE | `/api/quiz/alternativas/{id}` | ADMIN | Remover alternativa |
| POST | `/api/quiz/{id}/responder` | USER+ | Submeter respostas (calcula pontuação) |
| GET | `/api/quiz/{id}/ranking` | ❌ Público | Ver ranking do quiz |

## Autorização

A autorização é feita em dois níveis:

1. **SecurityFilterChain**: define regras gerais (endpoints públicos vs autenticados)
2. **Controller-level** via `AuthConfig`: validações específicas (`requireAdmin()`, `getAuthenticatedUserOrThrow()`)

## Regras de Negócio

1. **Tipos de Utilizador**: um utilizador pode ter múltiplos tipos simultaneamente (ex: ADMIN + ESCRITOR)
2. **Artigos Pendentes**: escritores criam artigos com status PENDENTE; só após aprovação do admin ficam visíveis. Admins criam artigos auto-aprovados.
3. **Parágrafos**: artigos são compostos por múltiplos parágrafos (título + texto), geridos separadamente.
4. **Jindungo**: artigos marcados com `jindungo=true` exigem solicitação especial para visualizar.
5. **Fórum Privado**: para comentar num fórum privado, o utilizador precisa solicitar e o admin aprovar.
6. **Denúncias**: utilizadores podem denunciar comentários (não os próprios); gera notificação automática para admins. O admin oculta o comentário, não o apaga.
7. **Quiz**: apenas autenticados podem responder; não autenticados veem apenas o ranking. Publicação requer que cada pergunta tenha pelo menos uma alternativa correta.
8. **SUPER_ADMIN**: não pode ser atribuído via API (apenas diretamente na base de dados).

## Como Executar

1. **Pré-requisitos**: Java 17+, MySQL
2. **Configurar banco de dados**: copiar `src/main/resources/application.yml.example` para `application.yml` e preencher as variáveis de ambiente:
   ```yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/economia_db?createDatabaseIfNotExists=true
       username: root
       password: ${DB_PASSWORD}
     jpa:
       hibernate.ddl-auto: update
       properties:
         hibernate:
           dialect: org.hibernate.dialect.MySQLDialect

   jwt:
     secret: ${JWT_SECRET}
     expiration: 86400000
   ```
3. **(Alternativa)** Definir variáveis de ambiente `DB_PASSWORD` e `JWT_SECRET` no sistema.
4. **Compilar e executar**:
   ```bash
   ./mvnw spring-boot:run
   ```
5. A API estará disponível em `http://localhost:8080/api`

---

## Estrutura das Classes

### Entities

```java
// User
@Entity @Table(name = "usuarios")
public class User {
    private Long id;
    private String nome;
    private String email;          // unique
    private String senha;
    private Set<UserType> tipos;   // @ElementCollection
    private LocalDateTime dataCriacao;
    private boolean ativo;
}

// Article
@Entity @Table(name = "artigos")
public class Article {
    private Long id;
    private String titulo;
    private int duracaoLeitura;
    private String imagem;
    private String video;
    private int visualizacoes;
    private LocalDateTime dataPublicacao;
    private ArticleStatus status;       // PENDENTE, APROVADO, REJEITADO
    private boolean jindungo;
    private Category categoria;         // @ManyToOne
    private User publicador;            // @ManyToOne
    private List<Comment> comentarios;  // @OneToMany
    private List<Paragraph> paragrafos; // @OneToMany @OrderBy
}

// Paragraph
@Entity @Table(name = "paragrafos")
public class Paragraph {
    private Long id;
    private String titulo;
    private String texto;     // @Lob LONGTEXT
    private Article artigo;   // @ManyToOne
}

// Category
@Entity @Table(name = "categorias")
public class Category {
    private Long id;
    private String nome;    // unique
}

// Comment
@Entity @Table(name = "comentarios")
public class Comment {
    private Long id;
    private String texto;
    private int likes;
    private boolean denunciado;
    private boolean oculto;
    private LocalDateTime dataCriacao;
    private User usuario;           // @ManyToOne
    private Article artigo;         // @ManyToOne (nullable)
    private Forum forum;            // @ManyToOne (nullable)
    private List<Report> denuncias; // @OneToMany
}

// Forum
@Entity @Table(name = "foruns")
public class Forum {
    private Long id;
    private String titulo;
    private String descricao;
    private ForumAccess acesso;   // PUBLICO, PRIVADO
    private User criador;         // @ManyToOne
    private Category categoria;   // @ManyToOne
    private List<Comment> comentarios;                // @OneToMany
    private List<ForumParticipationRequest> solicitacoes; // @OneToMany
}

// ForumParticipationRequest
@Entity @Table(name = "solicitacoes_forum")
public class ForumParticipationRequest {
    private Long id;
    private RequestStatus status;      // PENDENTE, APROVADO, RECUSADO
    private LocalDateTime dataSolicitacao;
    private User usuario;              // @ManyToOne
    private Forum forum;               // @ManyToOne
}

// Notification
@Entity @Table(name = "notificacoes")
public class Notification {
    private Long id;
    private NotificationType tipo; // DENUNCIA, NOVO_ARTIGO, ARTIGO_APROVADO, SOLICITACAO_FORUM
    private String mensagem;
    private boolean lida;
    private LocalDateTime dataCriacao;
    private User usuario;          // @ManyToOne
}

// Report
@Entity @Table(name = "denuncias")
public class Report {
    private Long id;
    private String motivo;
    private LocalDateTime dataCriacao;
    private ReportStatus status;        // PENDENTE, OCULTADO, RESOLVIDO
    private User denunciante;           // @ManyToOne
    private Comment comentario;         // @ManyToOne
}

// Quiz
@Entity @Table(name = "quiz")
public class Quiz {
    private Long id;
    private String titulo;
    private String descricao;
    private boolean publicado;
    private User criador;               // @ManyToOne
    private List<Question> perguntas;   // @OneToMany
}

// Question
@Entity @Table(name = "perguntas")
public class Question {
    private Long id;
    private String enunciado;
    private int pontuacao;
    private Quiz quiz;                      // @ManyToOne
    private List<Alternative> alternativas; // @OneToMany
}

// Alternative
@Entity @Table(name = "alternativas")
public class Alternative {
    private Long id;
    private String texto;
    private boolean correta;
    private Question pergunta; // @ManyToOne
}

// QuizAttempt
@Entity @Table(name = "tentativas_quiz")
public class QuizAttempt {
    private Long id;
    private int pontuacao;
    private LocalDateTime dataRealizacao;
    private User usuario;                // @ManyToOne
    private Quiz quiz;                   // @ManyToOne
    private List<UserAnswer> userAnswers; // @OneToMany
}

// UserAnswer
@Entity @Table(name = "respostas_usuario")
public class UserAnswer {
    private Long id;
    private QuizAttempt tentativa;  // @ManyToOne
    private Question pergunta;      // @ManyToOne
    private Alternative alternativa; // @ManyToOne
}
```

### DTOs — Requests

```java
// RegisterRequest
public class RegisterRequest {
    @NotBlank private String nome;
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 6) private String senha;
}

// LoginRequest
public class LoginRequest {
    @NotBlank @Email private String email;
    @NotBlank private String senha;
}

// ArticleRequest
public class ArticleRequest {
    @NotBlank private String titulo;
    private int duracaoLeitura;
    private String imagem;
    private String video;
    @NotNull private Long categoriaId;
    private List<ParagraphRequest> paragrafos;
}

// ParagraphRequest
public class ParagraphRequest {
    @NotBlank private String titulo;
    @NotBlank private String texto;
}

// CategoryRequest
public class CategoryRequest {
    @NotBlank private String nome;
}

// CommentRequest
public class CommentRequest {
    @NotBlank private String texto;
}

// ForumRequest
public class ForumRequest {
    @NotBlank private String titulo;
    @NotBlank private String descricao;
    @NotNull private ForumAccess acesso;
    @NotNull private Long categoriaId;
}

// QuizRequest
public class QuizRequest {
    @NotBlank private String titulo;
    @NotBlank private String descricao;
}

// QuestionRequest
public class QuestionRequest {
    @NotBlank private String enunciado;
    private int pontuacao;
}

// AlternativeRequest
public class AlternativeRequest {
    @NotBlank private String texto;
    private boolean correta;
}

// QuizAnswerRequest
public class QuizAnswerRequest {
    @NotNull private Long quizId;
    private List<Resposta> respostas;

    public record Resposta(
        @NotNull Long perguntaId,
        @NotNull Long alternativaId
    ) {}
}

// ReportRequest
public class ReportRequest {
    @NotNull private Long comentarioId;
    @NotBlank private String motivo;
}

// UpdateUserRequest
public class UpdateUserRequest {
    private String nome;
    private String email;
    private String senha;
}

// UpdateUserTypesRequest
public class UpdateUserTypesRequest {
    private Set<UserType> tipos;
}
```

### DTOs — Responses

```java
// LoginResponse
public class LoginResponse {
    private Long id;
    private String nome;
    private String email;
    private Set<UserType> tipos;
    private LocalDateTime dataCriacao;
    private String token;
}

// UserResponse
public class UserResponse {
    private Long id;
    private String nome;
    private String email;
    private Set<UserType> tipos;
    private LocalDateTime dataCriacao;
    private boolean ativo;
}

// ArticleResponse
public class ArticleResponse {
    private Long id;
    private String titulo;
    private int duracaoLeitura;
    private String imagem;
    private String video;
    private int visualizacoes;
    private LocalDateTime dataPublicacao;
    private ArticleStatus status;
    private boolean jindungo;
    private Long categoriaId;
    private String categoriaNome;
    private Long publicadorId;
    private String publicadorNome;
    private int totalComentarios;
    private List<ParagraphResponse> paragrafos;
}

// ParagraphResponse
public class ParagraphResponse {
    private Long id;
    private String titulo;
    private String texto;
}

// CategoryResponse
public class CategoryResponse {
    private Long id;
    private String nome;
}

// CommentResponse
public class CommentResponse {
    private Long id;
    private String texto;
    private int likes;
    private boolean oculto;
    private LocalDateTime dataCriacao;
    private Long usuarioId;
    private String usuarioNome;
}

// ForumResponse
public class ForumResponse {
    private Long id;
    private String titulo;
    private String descricao;
    private ForumAccess acesso;
    private Long criadorId;
    private String criadorNome;
    private Long categoriaId;
    private String categoriaNome;
    private int totalComentarios;
}

// QuizResponse
public class QuizResponse {
    private Long id;
    private String titulo;
    private String descricao;
    private boolean publicado;
    private Long criadorId;
    private String criadorNome;
    private List<QuestionResponse> perguntas;
}

// QuestionResponse
public class QuestionResponse {
    private Long id;
    private String enunciado;
    private int pontuacao;
    private List<AlternativeResponse> alternativas;
}

// AlternativeResponse
public class AlternativeResponse {
    private Long id;
    private String texto;
    private boolean correta;
}

// QuizAttemptResponse
public class QuizAttemptResponse {
    private Long id;
    private int pontuacao;
    private LocalDateTime dataRealizacao;
    private Long usuarioId;
    private String usuarioNome;
    private Long quizId;
    private String quizTitulo;
}

// NotificationResponse
public class NotificationResponse {
    private Long id;
    private NotificationType tipo;
    private String mensagem;
    private boolean lida;
    private LocalDateTime dataCriacao;
}

// ReportResponse
public class ReportResponse {
    private Long id;
    private String motivo;
    private LocalDateTime dataCriacao;
    private ReportStatus status;
    private Long denuncianteId;
    private String denuncianteNome;
    private Long comentarioId;
    private String comentarioTexto;
}
```
