package com.api.ecnomia_api;

import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.UserType;
import com.api.ecnomia_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class EcnomiaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcnomiaApiApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User admin = User.builder()
                    .nome("Administrador")
                    .email("admin@gmail.com")
                    .senha(passwordEncoder.encode("admin@123"))
                    .tipos(Set.of(UserType.ADMIN, UserType.ESCRITOR, UserType.REVISOR))
                    .build();
                userRepository.save(admin);
                System.out.println(">>> Admin criado: admin@gmail.com / admin@123");
            }
        };
    }

}
