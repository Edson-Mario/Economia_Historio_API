package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.UpdateUserRequest;
import com.api.ecnomia_api.dto.UpdateUserTypesRequest;
import com.api.ecnomia_api.dto.UserResponse;
import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.UserType;
import com.api.ecnomia_api.exception.BusinessException;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
            .map(UserResponse::fromEntity)
            .toList();
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado"));
        return UserResponse.fromEntity(user);
    }

    public UserResponse update(Long id, UpdateUserRequest request, User authenticatedUser) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado"));

        if (!user.getId().equals(authenticatedUser.getId()) &&
            !authenticatedUser.getTipos().contains(UserType.ADMIN) &&
            !authenticatedUser.getTipos().contains(UserType.SUPER_ADMIN)) {
            throw new BusinessException("Não tens permissão para editar este utilizador");
        }

        if (request.nome() != null) user.setNome(request.nome());
        if (request.email() != null) {
            if (!request.email().equals(user.getEmail()) &&
                userRepository.existsByEmail(request.email())) {
                throw new BusinessException("E-mail já em uso");
            }
            user.setEmail(request.email());
        }
        if (request.senha() != null) user.setSenha(request.senha());

        user = userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado"));
        user.setAtivo(false);
        userRepository.save(user);
    }

    public UserResponse updateTypes(Long id, UpdateUserTypesRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado"));

        if (request.tipos().contains(UserType.SUPER_ADMIN)) {
            throw new BusinessException("Não é possível atribuir SUPER_ADMIN diretamente");
        }

        user.setTipos(request.tipos());
        user = userRepository.save(user);
        return UserResponse.fromEntity(user);
    }
}
