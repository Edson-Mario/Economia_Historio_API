package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportRequest(
    @NotNull Long comentarioId,
    @NotBlank String motivo
) {}
