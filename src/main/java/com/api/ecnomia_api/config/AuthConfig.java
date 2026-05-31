package com.api.ecnomia_api.config;

import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.UserType;
import com.api.ecnomia_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthConfig {

    private final UserRepository userRepository;

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        String email = auth.getName();
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getAuthenticatedUserOrThrow() {
        User user = getAuthenticatedUser();
        if (user == null) {
            throw new com.api.ecnomia_api.exception.UnauthorizedException("Utilizador não autenticado");
        }
        return user;
    }

    public boolean hasType(UserType type) {
        User user = getAuthenticatedUser();
        return user != null && user.getTipos().contains(type);
    }

    public boolean isAdmin() {
        return hasType(UserType.ADMIN) || hasType(UserType.SUPER_ADMIN);
    }

    public void requireAdmin() {
        if (!isAdmin()) {
            throw new com.api.ecnomia_api.exception.UnauthorizedException("Apenas administradores podem realizar esta ação");
        }
    }

    public void requireAuthentication() {
        getAuthenticatedUserOrThrow();
    }
}
