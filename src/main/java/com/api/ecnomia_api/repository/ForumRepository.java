package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Forum;
import com.api.ecnomia_api.enums.ForumAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Long> {
    List<Forum> findByAcesso(ForumAccess acesso);
    List<Forum> findByCategoriaId(Long categoriaId);
    List<Forum> findByCriadorId(Long criadorId);
}
