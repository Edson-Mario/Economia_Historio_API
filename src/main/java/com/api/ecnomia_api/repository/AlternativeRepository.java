package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Alternative;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlternativeRepository extends JpaRepository<Alternative, Long> {
    List<Alternative> findByPerguntaId(Long perguntaId);
}
