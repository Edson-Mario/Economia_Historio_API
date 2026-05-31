package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArtigoIdOrderByDataCriacaoDesc(Long artigoId);
    List<Comment> findByForumIdOrderByDataCriacaoDesc(Long forumId);
    List<Comment> findByDenunciadoTrueAndOcultoFalse();
}
