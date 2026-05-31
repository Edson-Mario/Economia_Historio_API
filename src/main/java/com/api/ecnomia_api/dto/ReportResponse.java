package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Report;
import com.api.ecnomia_api.enums.ReportStatus;
import java.time.LocalDateTime;

public record ReportResponse(
    Long id,
    String motivo,
    LocalDateTime dataCriacao,
    ReportStatus status,
    Long denuncianteId,
    String denuncianteNome,
    Long comentarioId,
    String comentarioTexto
) {
    public static ReportResponse fromEntity(Report report) {
        return new ReportResponse(
            report.getId(),
            report.getMotivo(),
            report.getDataCriacao(),
            report.getStatus(),
            report.getDenunciante().getId(),
            report.getDenunciante().getNome(),
            report.getComentario().getId(),
            report.getComentario().getTexto()
        );
    }
}
