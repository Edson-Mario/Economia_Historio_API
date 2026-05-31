package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Paragraph;

public record ParagraphResponse(
    Long id,
    String titulo,
    String texto
) {
    public static ParagraphResponse fromEntity(Paragraph paragraph) {
        return new ParagraphResponse(
            paragraph.getId(),
            paragraph.getTitulo(),
            paragraph.getTexto()
        );
    }
}
