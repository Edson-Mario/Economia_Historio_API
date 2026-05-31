package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.LoginRequest;
import com.api.ecnomia_api.dto.LoginResponse;
import com.api.ecnomia_api.dto.RegisterRequest;
import com.api.ecnomia_api.dto.UserResponse;
import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.exception.BusinessException;
import com.api.ecnomia_api.repository.UserRepository;
import com.api.ecnomia_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já registado");
        }

        User user = User.builder()
            .nome(request.nome())
            .email(request.email())
            .senha(passwordEncoder.encode(request.senha()))
            .build();

        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return LoginResponse.fromEntity(user, token);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new BusinessException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), user.getSenha())) {
            throw new BusinessException("E-mail ou senha inválidos");
        }

        if (!user.isAtivo()) {
            throw new BusinessException("Conta desativada");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return LoginResponse.fromEntity(user, token);
    }
}
