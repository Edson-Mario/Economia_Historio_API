package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.Article;
import com.api.ecnomia_api.enums.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByStatus(ArticleStatus status);
    List<Article> findByCategoriaIdAndStatus(Long categoriaId, ArticleStatus status);
    List<Article> findByStatusOrderByDataPublicacaoDesc(ArticleStatus status);
    List<Article> findByJindungoAndStatus(boolean jindungo, ArticleStatus status);
}
