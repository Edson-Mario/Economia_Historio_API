package com.api.ecnomia_api.dto;

public record UpdateUserRequest(
    String nome,
    String email,
    String senha
) {}
