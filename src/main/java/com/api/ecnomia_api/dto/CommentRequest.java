package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
    @NotBlank String texto
) {}
