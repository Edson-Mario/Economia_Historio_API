package com.api.ecnomia_api.controller;

import com.api.ecnomia_api.dto.*;
import com.api.ecnomia_api.service.UserService;
import com.api.ecnomia_api.config.AuthConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthConfig authConfig;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        authConfig.requireAdmin();
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request, authConfig.getAuthenticatedUserOrThrow()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authConfig.requireAdmin();
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/tipos")
    public ResponseEntity<UserResponse> updateTypes(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateUserTypesRequest request) {
        authConfig.requireAdmin();
        return ResponseEntity.ok(userService.updateTypes(id, request));
    }
}
