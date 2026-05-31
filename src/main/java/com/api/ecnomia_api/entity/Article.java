package com.api.ecnomia_api.entity;

import com.api.ecnomia_api.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artigos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "duracao_leitura")
    private int duracaoLeitura;

    private String imagem;

    private String video;

    @Column(nullable = false)
    @Builder.Default
    private int visualizacoes = 0;

    @Column(name = "data_publicacao")
    private LocalDateTime dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ArticleStatus status = ArticleStatus.PENDENTE;

    @Column(nullable = false)
    @Builder.Default
    private boolean jindungo = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Category categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicador_id", nullable = false)
    private User publicador;

    @OneToMany(mappedBy = "artigo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "artigo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private List<Paragraph> paragrafos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.status == null) this.status = ArticleStatus.PENDENTE;
        if (this.publicador != null && this.publicador.getTipos().contains(com.api.ecnomia_api.enums.UserType.ADMIN)) {
            this.status = ArticleStatus.APROVADO;
            this.dataPublicacao = LocalDateTime.now();
        }
    }

    public void incrementarVisualizacao() {
        this.visualizacoes++;
    }
}
