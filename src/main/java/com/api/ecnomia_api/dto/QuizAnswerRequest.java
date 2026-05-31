package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record QuizAnswerRequest(
    @NotNull Long quizId,
    List<Resposta> respostas
) {
    public record Resposta(
        @NotNull Long perguntaId,
        @NotNull Long alternativaId
    ) {}
}
