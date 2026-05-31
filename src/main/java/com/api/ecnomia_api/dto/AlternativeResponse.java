package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Alternative;

public record AlternativeResponse(
    Long id,
    String texto,
    boolean correta
) {
    public static AlternativeResponse fromEntity(Alternative alternative) {
        return new AlternativeResponse(
            alternative.getId(),
            alternative.getTexto(),
            alternative.isCorreta()
        );
    }

    public static AlternativeResponse fromEntitySemCorreta(Alternative alternative) {
        return new AlternativeResponse(
            alternative.getId(),
            alternative.getTexto(),
            false
        );
    }
}
