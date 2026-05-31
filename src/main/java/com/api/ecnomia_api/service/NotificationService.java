package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.NotificationResponse;
import com.api.ecnomia_api.entity.Notification;
import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> findByUser(User user) {
        return notificationRepository.findByUsuarioIdOrderByDataCriacaoDesc(user.getId())
            .stream().map(NotificationResponse::fromEntity).toList();
    }

    public long countNonRead(User user) {
        return notificationRepository.countByUsuarioIdAndLidaFalse(user.getId());
    }

    public void markAsRead(Long id, User user) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada"));

        if (!notification.getUsuario().getId().equals(user.getId())) {
            throw new com.api.ecnomia_api.exception.UnauthorizedException("Esta notificação não te pertence");
        }

        notification.setLida(true);
        notificationRepository.save(notification);
    }
}
