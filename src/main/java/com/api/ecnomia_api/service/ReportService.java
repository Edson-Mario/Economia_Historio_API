package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.ReportRequest;
import com.api.ecnomia_api.dto.ReportResponse;
import com.api.ecnomia_api.entity.*;
import com.api.ecnomia_api.enums.NotificationType;
import com.api.ecnomia_api.enums.ReportStatus;
import com.api.ecnomia_api.enums.UserType;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReportResponse create(ReportRequest request, User denunciante) {
        Comment comment = commentRepository.findById(request.comentarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado"));

        if (comment.getUsuario().getId().equals(denunciante.getId())) {
            throw new com.api.ecnomia_api.exception.BusinessException("Não podes denunciar o teu próprio comentário");
        }

        Report report = Report.builder()
            .motivo(request.motivo())
            .denunciante(denunciante)
            .comentario(comment)
            .build();

        comment.setDenunciado(true);
        commentRepository.save(comment);
        report = reportRepository.save(report);

        List<User> admins = userRepository.findByTiposContaining(UserType.ADMIN);
        for (User admin : admins) {
            Notification notification = Notification.builder()
                .tipo(NotificationType.DENUNCIA)
                .mensagem("Novo comentário denunciado por " + denunciante.getNome() + ": " + request.motivo())
                .usuario(admin)
                .build();
            notificationRepository.save(notification);
        }

        return ReportResponse.fromEntity(report);
    }

    public List<ReportResponse> findAllPendentes() {
        return reportRepository.findByStatus(ReportStatus.PENDENTE)
            .stream().map(ReportResponse::fromEntity).toList();
    }

    @Transactional
    public void ocultar(Long id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Denúncia não encontrada"));

        Comment comment = report.getComentario();
        comment.setOculto(true);
        commentRepository.save(comment);

        report.setStatus(ReportStatus.OCULTADO);
        reportRepository.save(report);
    }
}
