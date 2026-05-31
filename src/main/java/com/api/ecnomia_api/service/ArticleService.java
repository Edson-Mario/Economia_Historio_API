package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.ArticleRequest;
import com.api.ecnomia_api.dto.ArticleResponse;
import com.api.ecnomia_api.dto.ParagraphRequest;
import com.api.ecnomia_api.dto.ParagraphResponse;
import com.api.ecnomia_api.entity.Article;
import com.api.ecnomia_api.entity.Category;
import com.api.ecnomia_api.entity.Paragraph;
import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.ArticleStatus;
import com.api.ecnomia_api.enums.UserType;
import com.api.ecnomia_api.exception.BusinessException;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.ArticleRepository;
import com.api.ecnomia_api.repository.CategoryRepository;
import com.api.ecnomia_api.repository.ParagraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ParagraphRepository paragraphRepository;

    public List<ArticleResponse> findAllAprovados() {
        return articleRepository.findByStatusOrderByDataPublicacaoDesc(ArticleStatus.APROVADO)
            .stream().map(ArticleResponse::fromEntity).toList();
    }

    public List<ArticleResponse> findByCategoria(Long categoriaId) {
        return articleRepository.findByCategoriaIdAndStatus(categoriaId, ArticleStatus.APROVADO)
            .stream().map(ArticleResponse::fromEntity).toList();
    }

    public List<ArticleResponse> findPendentes() {
        return articleRepository.findByStatus(ArticleStatus.PENDENTE)
            .stream().map(ArticleResponse::fromEntity).toList();
    }

    @Transactional
    public ArticleResponse findById(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));
        article.incrementarVisualizacao();
        article = articleRepository.save(article);
        return ArticleResponse.fromEntity(article);
    }

    public ArticleResponse create(ArticleRequest request, User publicador) {
        Category category = categoryRepository.findById(request.categoriaId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Article article = Article.builder()
            .titulo(request.titulo())
            .duracaoLeitura(request.duracaoLeitura())
            .imagem(request.imagem())
            .video(request.video())
            .categoria(category)
            .publicador(publicador)
            .build();

        if (request.paragrafos() != null) {
            for (ParagraphRequest pr : request.paragrafos()) {
                Paragraph p = Paragraph.builder()
                    .titulo(pr.titulo())
                    .texto(pr.texto())
                    .artigo(article)
                    .build();
                article.getParagrafos().add(p);
            }
        }

        if (publicador.getTipos().contains(UserType.ADMIN) ||
            publicador.getTipos().contains(UserType.SUPER_ADMIN)) {
            article.setStatus(ArticleStatus.APROVADO);
            article.setDataPublicacao(LocalDateTime.now());
        }

        article = articleRepository.save(article);
        return ArticleResponse.fromEntity(article);
    }

    public ArticleResponse update(Long id, ArticleRequest request) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        Category category = categoryRepository.findById(request.categoriaId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        article.setTitulo(request.titulo());
        article.setDuracaoLeitura(request.duracaoLeitura());
        article.setImagem(request.imagem());
        article.setVideo(request.video());
        article.setCategoria(category);

        article.getParagrafos().clear();
        if (request.paragrafos() != null) {
            for (ParagraphRequest pr : request.paragrafos()) {
                Paragraph p = Paragraph.builder()
                    .titulo(pr.titulo())
                    .texto(pr.texto())
                    .artigo(article)
                    .build();
                article.getParagrafos().add(p);
            }
        }

        article = articleRepository.save(article);
        return ArticleResponse.fromEntity(article);
    }

    public ParagraphResponse addParagraph(Long articleId, ParagraphRequest request) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        Paragraph paragraph = Paragraph.builder()
            .titulo(request.titulo())
            .texto(request.texto())
            .artigo(article)
            .build();

        article.getParagrafos().add(paragraph);
        articleRepository.save(article);
        return ParagraphResponse.fromEntity(paragraph);
    }

    public ParagraphResponse updateParagraph(Long paragraphId, ParagraphRequest request) {
        Paragraph paragraph = paragraphRepository.findById(paragraphId)
            .orElseThrow(() -> new ResourceNotFoundException("Parágrafo não encontrado"));

        paragraph.setTitulo(request.titulo());
        paragraph.setTexto(request.texto());
        paragraph = paragraphRepository.save(paragraph);
        return ParagraphResponse.fromEntity(paragraph);
    }

    public void removeParagraph(Long paragraphId) {
        Paragraph paragraph = paragraphRepository.findById(paragraphId)
            .orElseThrow(() -> new ResourceNotFoundException("Parágrafo não encontrado"));
        paragraphRepository.delete(paragraph);
    }

    public void delete(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));
        articleRepository.delete(article);
    }

    public ArticleResponse aprovar(Long id, boolean aprovado) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        if (aprovado) {
            article.setStatus(ArticleStatus.APROVADO);
            article.setDataPublicacao(LocalDateTime.now());
        } else {
            article.setStatus(ArticleStatus.REJEITADO);
        }

        article = articleRepository.save(article);
        return ArticleResponse.fromEntity(article);
    }

    public ArticleResponse toggleJindungo(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));
        article.setJindungo(!article.isJindungo());
        article = articleRepository.save(article);
        return ArticleResponse.fromEntity(article);
    }
}
