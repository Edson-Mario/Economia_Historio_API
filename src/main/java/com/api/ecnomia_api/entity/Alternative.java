package com.api.ecnomia_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alternativas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    @Builder.Default
    private boolean correta = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Question pergunta;
}
