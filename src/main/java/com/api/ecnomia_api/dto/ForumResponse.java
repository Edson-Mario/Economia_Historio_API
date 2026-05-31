package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Forum;
import com.api.ecnomia_api.enums.ForumAccess;

public record ForumResponse(
    Long id,
    String titulo,
    String descricao,
    ForumAccess acesso,
    Long criadorId,
    String criadorNome,
    Long categoriaId,
    String categoriaNome,
    int totalComentarios
) {
    public static ForumResponse fromEntity(Forum forum) {
        return new ForumResponse(
            forum.getId(),
            forum.getTitulo(),
            forum.getDescricao(),
            forum.getAcesso(),
            forum.getCriador().getId(),
            forum.getCriador().getNome(),
            forum.getCategoria().getId(),
            forum.getCategoria().getNome(),
            forum.getComentarios().size()
        );
    }
}
