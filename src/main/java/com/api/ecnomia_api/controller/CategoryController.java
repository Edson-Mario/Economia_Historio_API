package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.CategoryRequest;
import com.api.ecnomia_api.dto.CategoryResponse;
import com.api.ecnomia_api.service.CategoryService;
import com.api.ecnomia_api.config.AuthConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthConfig authConfig;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        authConfig.requireAdmin();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authConfig.requireAdmin();
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
