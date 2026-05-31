package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Report;
import com.api.ecnomia_api.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(ReportStatus status);
    List<Report> findByComentarioId(Long comentarioId);
}
