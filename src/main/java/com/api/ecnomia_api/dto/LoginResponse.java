package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.UserType;
import java.time.LocalDateTime;
import java.util.Set;

public record LoginResponse(
    Long id,
    String nome,
    String email,
    Set<UserType> tipos,
    LocalDateTime dataCriacao,
    String token
) {
    public static LoginResponse fromEntity(User user, String token) {
        return new LoginResponse(
            user.getId(),
            user.getNome(),
            user.getEmail(),
            user.getTipos(),
            user.getDataCriacao(),
            token
        );
    }
}
