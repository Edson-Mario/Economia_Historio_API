package com.api.ecnomia_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "respostas_usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tentativa_id", nullable = false)
    private QuizAttempt tentativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Question pergunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_id", nullable = false)
    private Alternative alternativa;
}
