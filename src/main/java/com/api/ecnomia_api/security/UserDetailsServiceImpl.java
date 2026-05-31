package com.api.ecnomia_api.security;

import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado: " + email));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getSenha(),
            user.isAtivo(),
            true, true, true,
            user.getTipos().stream()
                .map(tipo -> new SimpleGrantedAuthority("ROLE_" + tipo.name()))
                .collect(Collectors.toList())
        );
    }
}
