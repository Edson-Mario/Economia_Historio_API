package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.enums.ForumAccess;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ForumRequest(
    @NotBlank String titulo,
    @NotBlank String descricao,
    @NotNull ForumAccess acesso,
    @NotNull Long categoriaId
) {}
