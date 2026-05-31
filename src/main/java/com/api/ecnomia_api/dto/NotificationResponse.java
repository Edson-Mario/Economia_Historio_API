package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Notification;
import com.api.ecnomia_api.enums.NotificationType;
import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    NotificationType tipo,
    String mensagem,
    boolean lida,
    LocalDateTime dataCriacao
) {
    public static NotificationResponse fromEntity(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getTipo(),
            notification.getMensagem(),
            notification.isLida(),
            notification.getDataCriacao()
        );
    }
}
