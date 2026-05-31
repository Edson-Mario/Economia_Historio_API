package com.api.ecnomia_api.entity;

import com.api.ecnomia_api.enums.ForumAccess;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "foruns")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ForumAccess acesso = ForumAccess.PUBLICO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private User criador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Category categoria;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ForumParticipationRequest> solicitacoes = new ArrayList<>();
}
