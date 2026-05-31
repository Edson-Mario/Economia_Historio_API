package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
    List<Paragraph> findByArtigoIdOrderByIdAsc(Long artigoId);
}
