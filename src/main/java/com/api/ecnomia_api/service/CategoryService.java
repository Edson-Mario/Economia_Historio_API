package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.CategoryRequest;
import com.api.ecnomia_api.dto.CategoryResponse;
import com.api.ecnomia_api.entity.Category;
import com.api.ecnomia_api.exception.BusinessException;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponse::fromEntity)
            .toList();
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNome(request.nome())) {
            throw new BusinessException("Categoria já existe");
        }
        Category category = Category.builder()
            .nome(request.nome())
            .build();
        category = categoryRepository.save(category);
        return CategoryResponse.fromEntity(category);
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        categoryRepository.delete(category);
    }
}
