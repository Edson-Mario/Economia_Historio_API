package com.api.ecnomia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ArticleRequest(
    @NotBlank String titulo,
    int duracaoLeitura,
    String imagem,
    String video,
    @NotNull Long categoriaId,
    List<ParagraphRequest> paragrafos
) {}
