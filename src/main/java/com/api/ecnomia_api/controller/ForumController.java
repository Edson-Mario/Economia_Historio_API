package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.ForumRequest;
import com.api.ecnomia_api.dto.ForumResponse;
import com.api.ecnomia_api.entity.ForumParticipationRequest;
import com.api.ecnomia_api.service.ForumService;
import com.api.ecnomia_api.config.AuthConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foruns")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final AuthConfig authConfig;

    @GetMapping
    public ResponseEntity<List<ForumResponse>> findAll() {
        return ResponseEntity.ok(forumService.findAll());
    }

    @PostMapping
    public ResponseEntity<ForumResponse> create(@Valid @RequestBody ForumRequest request) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(forumService.create(request, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.findById(id));
    }

    @PostMapping("/{id}/solicitar")
    public ResponseEntity<Void> solicitarParticipacao(@PathVariable Long id) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        forumService.solicitarParticipacao(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/solicitacoes")
    public ResponseEntity<List<ForumParticipationRequest>> findSolicitacoes() {
        authConfig.requireAdmin();
        return ResponseEntity.ok(forumService.findSolicitacoesPendentes());
    }

    @PutMapping("/solicitacoes/{id}")
    public ResponseEntity<Void> processarSolicitacao(@PathVariable Long id,
                                                     @RequestParam boolean aprovado) {
        authConfig.requireAdmin();
        forumService.processarSolicitacao(id, aprovado);
        return ResponseEntity.ok().build();
    }
}
