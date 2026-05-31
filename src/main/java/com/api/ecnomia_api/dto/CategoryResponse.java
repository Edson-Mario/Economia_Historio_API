package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.Category;

public record CategoryResponse(Long id, String nome) {
    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(category.getId(), category.getNome());
    }
}
