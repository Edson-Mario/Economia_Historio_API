package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Article;
import com.api.ecnomia_api.enums.ArticleStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponse(
    Long id,
    String titulo,
    int duracaoLeitura,
    String imagem,
    String video,
    int visualizacoes,
    LocalDateTime dataPublicacao,
    ArticleStatus status,
    boolean jindungo,
    Long categoriaId,
    String categoriaNome,
    Long publicadorId,
    String publicadorNome,
    int totalComentarios,
    List<ParagraphResponse> paragrafos
) {
    public static ArticleResponse fromEntity(Article article) {
        return new ArticleResponse(
            article.getId(),
            article.getTitulo(),
            article.getDuracaoLeitura(),
            article.getImagem(),
            article.getVideo(),
            article.getVisualizacoes(),
            article.getDataPublicacao(),
            article.getStatus(),
            article.isJindungo(),
            article.getCategoria().getId(),
            article.getCategoria().getNome(),
            article.getPublicador().getId(),
            article.getPublicador().getNome(),
            article.getComentarios().size(),
            article.getParagrafos().stream().map(ParagraphResponse::fromEntity).toList()
        );
    }
}
