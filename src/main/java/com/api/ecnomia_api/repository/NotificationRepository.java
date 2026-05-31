package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);
    List<Notification> findByUsuarioIdAndLidaFalse(Long usuarioId);
    long countByUsuarioIdAndLidaFalse(Long usuarioId);
}
