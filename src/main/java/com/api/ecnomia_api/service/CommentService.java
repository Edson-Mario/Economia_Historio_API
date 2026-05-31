package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.CommentRequest;
import com.api.ecnomia_api.dto.CommentResponse;
import com.api.ecnomia_api.entity.*;
import com.api.ecnomia_api.exception.BusinessException;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final ForumRepository forumRepository;
    private final ForumParticipationRequestRepository participationRequestRepository;

    public List<CommentResponse> findByArtigo(Long artigoId) {
        return commentRepository.findByArtigoIdOrderByDataCriacaoDesc(artigoId)
            .stream()
            .filter(c -> !c.isOculto())
            .map(CommentResponse::fromEntity)
            .toList();
    }

    public List<CommentResponse> findByForum(Long forumId) {
        return commentRepository.findByForumIdOrderByDataCriacaoDesc(forumId)
            .stream()
            .filter(c -> !c.isOculto())
            .map(CommentResponse::fromEntity)
            .toList();
    }

    public CommentResponse addToArtigo(Long artigoId, CommentRequest request, User usuario) {
        Article article = articleRepository.findById(artigoId)
            .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        Comment comment = Comment.builder()
            .texto(request.texto())
            .usuario(usuario)
            .artigo(article)
            .build();

        comment = commentRepository.save(comment);
        return CommentResponse.fromEntity(comment);
    }

    public CommentResponse addToForum(Long forumId, CommentRequest request, User usuario) {
        Forum forum = forumRepository.findById(forumId)
            .orElseThrow(() -> new ResourceNotFoundException("Fórum não encontrado"));

        if (forum.getAcesso() == com.api.ecnomia_api.enums.ForumAccess.PRIVADO) {
            boolean autorizado = participationRequestRepository
                .existsByUsuarioIdAndForumIdAndStatus(usuario.getId(), forumId,
                    com.api.ecnomia_api.enums.RequestStatus.APROVADO);
            if (!autorizado && !forum.getCriador().getId().equals(usuario.getId())) {
                throw new BusinessException("Não tens permissão para comentar neste fórum privado");
            }
        }

        Comment comment = Comment.builder()
            .texto(request.texto())
            .usuario(usuario)
            .forum(forum)
            .build();

        comment = commentRepository.save(comment);
        return CommentResponse.fromEntity(comment);
    }

    public void like(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado"));
        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);
    }

    public void delete(Long id, User usuario) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado"));

        boolean isOwner = comment.getUsuario().getId().equals(usuario.getId());
        boolean isAdmin = usuario.getTipos().contains(com.api.ecnomia_api.enums.UserType.ADMIN) ||
                          usuario.getTipos().contains(com.api.ecnomia_api.enums.UserType.SUPER_ADMIN);

        if (!isOwner && !isAdmin) {
            throw new BusinessException("Não tens permissão para remover este comentário");
        }

        commentRepository.delete(comment);
    }
}
