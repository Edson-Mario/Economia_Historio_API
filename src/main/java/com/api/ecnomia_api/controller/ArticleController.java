package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.ArticleRequest;
import com.api.ecnomia_api.dto.ArticleResponse;
import com.api.ecnomia_api.dto.ParagraphRequest;
import com.api.ecnomia_api.dto.ParagraphResponse;
import com.api.ecnomia_api.service.ArticleService;
import com.api.ecnomia_api.config.AuthConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artigos")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final AuthConfig authConfig;

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll() {
        return ResponseEntity.ok(articleService.findAllAprovados());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ArticleResponse>> findByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(articleService.findByCategoria(categoriaId));
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<ArticleResponse>> findPendentes() {
        authConfig.requireAdmin();
        return ResponseEntity.ok(articleService.findPendentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleRequest request) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(articleService.create(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ArticleRequest request) {
        authConfig.requireAdmin();
        return ResponseEntity.ok(articleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authConfig.requireAdmin();
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<ArticleResponse> aprovar(@PathVariable Long id,
                                                   @RequestParam boolean aprovado) {
        authConfig.requireAdmin();
        return ResponseEntity.ok(articleService.aprovar(id, aprovado));
    }

    @PutMapping("/{id}/jindungo")
    public ResponseEntity<ArticleResponse> toggleJindungo(@PathVariable Long id) {
        authConfig.requireAdmin();
        return ResponseEntity.ok(articleService.toggleJindungo(id));
    }

    @PostMapping("/{id}/paragrafos")
    public ResponseEntity<ParagraphResponse> addParagraph(@PathVariable Long id,
                                                           @Valid @RequestBody ParagraphRequest request) {
        authConfig.requireAdmin();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(articleService.addParagraph(id, request));
    }

    @PutMapping("/{id}/paragrafos/{paragraphId}")
    public ResponseEntity<ParagraphResponse> updateParagraph(@PathVariable Long id,
                                                              @PathVariable Long paragraphId,
                                                              @Valid @RequestBody ParagraphRequest request) {
        authConfig.requireAdmin();
        return ResponseEntity.ok(articleService.updateParagraph(paragraphId, request));
    }

    @DeleteMapping("/{id}/paragrafos/{paragraphId}")
    public ResponseEntity<Void> removeParagraph(@PathVariable Long id,
                                                @PathVariable Long paragraphId) {
        authConfig.requireAdmin();
        articleService.removeParagraph(paragraphId);
        return ResponseEntity.noContent().build();
    }
}
