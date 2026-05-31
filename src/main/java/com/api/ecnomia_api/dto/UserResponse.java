package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.UserType;
import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
    Long id,
    String nome,
    String email,
    Set<UserType> tipos,
    LocalDateTime dataCriacao,
    boolean ativo
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getNome(),
            user.getEmail(),
            user.getTipos(),
            user.getDataCriacao(),
            user.isAtivo()
        );
    }
}
