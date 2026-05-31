package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.NotificationResponse;
import com.api.ecnomia_api.service.NotificationService;
import com.api.ecnomia_api.config.AuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthConfig authConfig;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAll() {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.ok(notificationService.findByUser(user));
    }

    @GetMapping("/nao-lidas")
    public ResponseEntity<Long> countNonRead() {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.ok(notificationService.countNonRead(user));
    }

    @PutMapping("/{id}/ler")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        notificationService.markAsRead(id, user);
        return ResponseEntity.ok().build();
    }
}
