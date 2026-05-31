package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
    Long id,
    String texto,
    int likes,
    boolean oculto,
    LocalDateTime dataCriacao,
    Long usuarioId,
    String usuarioNome
) {
    public static CommentResponse fromEntity(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getTexto(),
            comment.getLikes(),
            comment.isOculto(),
            comment.getDataCriacao(),
            comment.getUsuario().getId(),
            comment.getUsuario().getNome()
        );
    }
}
