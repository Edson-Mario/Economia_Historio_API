package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionRequest(
    @NotBlank String enunciado,
    int pontuacao
) {}
