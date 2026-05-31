package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Question;
import java.util.List;

public record QuestionResponse(
    Long id,
    String enunciado,
    int pontuacao,
    List<AlternativeResponse> alternativas
) {
    public static QuestionResponse fromEntity(Question question) {
        return new QuestionResponse(
            question.getId(),
            question.getEnunciado(),
            question.getPontuacao(),
            question.getAlternativas().stream().map(AlternativeResponse::fromEntity).toList()
        );
    }

    public static QuestionResponse fromEntitySemCorreta(Question question) {
        return new QuestionResponse(
            question.getId(),
            question.getEnunciado(),
            question.getPontuacao(),
            question.getAlternativas().stream().map(AlternativeResponse::fromEntitySemCorreta).toList()
        );
    }
}
