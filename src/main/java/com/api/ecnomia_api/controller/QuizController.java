package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.*;
import com.api.ecnomia_api.service.QuizService;
import com.api.ecnomia_api.config.AuthConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final AuthConfig authConfig;

    @GetMapping
    public ResponseEntity<List<QuizResponse>> findAll() {
        return ResponseEntity.ok(quizService.findAll(true));
    }

    @PostMapping
    public ResponseEntity<QuizResponse> create(@Valid @RequestBody QuizRequest request) {
        authConfig.requireAdmin();
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(quizService.create(request, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> findById(@PathVariable Long id) {
        boolean showAnswers = authConfig.isAdmin();
        return ResponseEntity.ok(quizService.findById(id, showAnswers));
    }

    @PutMapping("/{id}/publicar")
    public ResponseEntity<QuizResponse> publicar(@PathVariable Long id) {
        authConfig.requireAdmin();
        return ResponseEntity.ok(quizService.publicar(id));
    }

    @PostMapping("/{id}/perguntas")
    public ResponseEntity<QuestionResponse> addQuestion(@PathVariable Long id,
                                                         @Valid @RequestBody QuestionRequest request) {
        authConfig.requireAdmin();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(quizService.addQuestion(id, request));
    }

    @DeleteMapping("/perguntas/{id}")
    public ResponseEntity<Void> removeQuestion(@PathVariable Long id) {
        authConfig.requireAdmin();
        quizService.removeQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/perguntas/{id}/alternativas")
    public ResponseEntity<AlternativeResponse> addAlternative(@PathVariable Long id,
                                                               @Valid @RequestBody AlternativeRequest request) {
        authConfig.requireAdmin();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(quizService.addAlternative(id, request));
    }

    @DeleteMapping("/alternativas/{id}")
    public ResponseEntity<Void> removeAlternative(@PathVariable Long id) {
        authConfig.requireAdmin();
        quizService.removeAlternative(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/responder")
    public ResponseEntity<QuizAttemptResponse> responder(@PathVariable Long id,
                                                          @Valid @RequestBody QuizAnswerRequest request) {
        var user = authConfig.getAuthenticatedUserOrThrow();
        return ResponseEntity.ok(quizService.responder(id, request, user));
    }

    @GetMapping("/{id}/ranking")
    public ResponseEntity<List<QuizAttemptResponse>> getRanking(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getRanking(id));
    }
}
