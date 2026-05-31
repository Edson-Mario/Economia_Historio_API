package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.CommentRequest;
import com.api.ecnomia_api.dto.CommentResponse;
import com.api.ecnomia_api.service.CommentService;
import com.api.ecnomia_api.config.AuthConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthConfig authConfig;

    @GetMapping("/api/artigos/{artigoId}/comentarios")
    public ResponseEntity<List<CommentResponse>> findByArtigo(@PathVariable Long artigoId) {
        return ResponseEntity.ok(commentService.findByArtigo(artigoId));
    }

    @GetMapping("/api/foruns/{forumId}/comentarios")
    public ResponseEntity<List<CommentResponse>> findByForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(commentService.findByForum(forumId));
    }

    @PostMapping("/api/artigos/{artigoId}/comentarios")
    public ResponseEntity<CommentResponse> addToArtigo(@PathVariable Long artigoId,
                                                        @Valid @RequestBody CommentRequest request) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.addToArtigo(artigoId, request, user));
    }

    @PostMapping("/api/foruns/{forumId}/comentarios")
    public ResponseEntity<CommentResponse> addToForum(@PathVariable Long forumId,
                                                       @Valid @RequestBody CommentRequest request) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.addToForum(forumId, request, user));
    }

    @PutMapping("/api/comentarios/{id}/like")
    public ResponseEntity<Void> like(@PathVariable Long id) {
        commentService.like(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/comentarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        commentService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
