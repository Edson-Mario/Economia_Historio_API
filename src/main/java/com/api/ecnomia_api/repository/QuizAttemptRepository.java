package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByQuizIdOrderByPontuacaoDesc(Long quizId);
    List<QuizAttempt> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndQuizId(Long usuarioId, Long quizId);
}
