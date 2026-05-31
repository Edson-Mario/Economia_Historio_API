package com.api.ecnomia_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paragrafos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paragraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String texto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artigo_id", nullable = false)
    private Article artigo;
}
