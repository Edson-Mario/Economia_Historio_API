package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Quiz;
import java.util.List;

public record QuizResponse(
    Long id,
    String titulo,
    String descricao,
    boolean publicado,
    Long criadorId,
    String criadorNome,
    List<QuestionResponse> perguntas
) {
    public static QuizResponse fromEntity(Quiz quiz) {
        return new QuizResponse(
            quiz.getId(),
            quiz.getTitulo(),
            quiz.getDescricao(),
            quiz.isPublicado(),
            quiz.getCriador().getId(),
            quiz.getCriador().getNome(),
            quiz.getPerguntas().stream().map(QuestionResponse::fromEntity).toList()
        );
    }

    public static QuizResponse fromEntitySemRespostas(Quiz quiz) {
        return new QuizResponse(
            quiz.getId(),
            quiz.getTitulo(),
            quiz.getDescricao(),
            quiz.isPublicado(),
            quiz.getCriador().getId(),
            quiz.getCriador().getNome(),
            quiz.getPerguntas().stream().map(QuestionResponse::fromEntitySemCorreta).toList()
        );
    }
}
