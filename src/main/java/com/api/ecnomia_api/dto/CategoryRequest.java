package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank String nome
) {}
