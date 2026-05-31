package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotBlank;

public record QuizRequest(
    @NotBlank String titulo,
    @NotBlank String descricao
) {}
