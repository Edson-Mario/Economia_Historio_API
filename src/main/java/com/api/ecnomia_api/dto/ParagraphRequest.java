package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ParagraphRequest(
    @NotBlank String titulo,
    @NotBlank String texto
) {}
