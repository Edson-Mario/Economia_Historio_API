package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByPublicadoTrue();
    List<Quiz> findByCriadorId(Long criadorId);
}
