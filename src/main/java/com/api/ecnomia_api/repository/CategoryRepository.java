package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNome(String nome);
    boolean existsByNome(String nome);
}
