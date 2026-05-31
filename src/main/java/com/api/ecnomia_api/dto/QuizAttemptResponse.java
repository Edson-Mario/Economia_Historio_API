package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.QuizAttempt;
import java.time.LocalDateTime;

public record QuizAttemptResponse(
    Long id,
    int pontuacao,
    LocalDateTime dataRealizacao,
    Long usuarioId,
    String usuarioNome,
    Long quizId,
    String quizTitulo
) {
    public static QuizAttemptResponse fromEntity(QuizAttempt attempt) {
        return new QuizAttemptResponse(
            attempt.getId(),
            attempt.getPontuacao(),
            attempt.getDataRealizacao(),
            attempt.getUsuario().getId(),
            attempt.getUsuario().getNome(),
            attempt.getQuiz().getId(),
            attempt.getQuiz().getTitulo()
        );
    }
}
