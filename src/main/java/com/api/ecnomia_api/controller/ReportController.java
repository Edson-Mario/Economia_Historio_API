package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.ReportRequest;
import com.api.ecnomia_api.dto.ReportResponse;
import com.api.ecnomia_api.service.ReportService;
import com.api.ecnomia_api.config.AuthConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final AuthConfig authConfig;

    @PostMapping
    public ResponseEntity<ReportResponse> create(@Valid @RequestBody ReportRequest request) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reportService.create(request, user));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> findAll() {
        authConfig.requireAdmin();
        return ResponseEntity.ok(reportService.findAllPendentes());
    }

    @PutMapping("/{id}/ocultar")
    public ResponseEntity<Void> ocultar(@PathVariable Long id) {
        authConfig.requireAdmin();
        reportService.ocultar(id);
        return ResponseEntity.ok().build();
    }
}
