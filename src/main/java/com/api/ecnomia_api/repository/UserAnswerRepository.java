package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByTentativaId(Long tentativaId);
}
