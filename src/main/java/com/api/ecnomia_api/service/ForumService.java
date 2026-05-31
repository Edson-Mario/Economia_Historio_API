package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.ForumRequest;
import com.api.ecnomia_api.dto.ForumResponse;
import com.api.ecnomia_api.entity.Category;
import com.api.ecnomia_api.entity.Forum;
import com.api.ecnomia_api.entity.ForumParticipationRequest;
import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.ForumAccess;
import com.api.ecnomia_api.enums.RequestStatus;
import com.api.ecnomia_api.exception.BusinessException;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.CategoryRepository;
import com.api.ecnomia_api.repository.ForumParticipationRequestRepository;
import com.api.ecnomia_api.repository.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final CategoryRepository categoryRepository;
    private final ForumParticipationRequestRepository participationRequestRepository;

    public List<ForumResponse> findAll() {
        return forumRepository.findByAcesso(ForumAccess.PUBLICO)
            .stream().map(ForumResponse::fromEntity).toList();
    }

    public ForumResponse findById(Long id) {
        Forum forum = forumRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fórum não encontrado"));
        return ForumResponse.fromEntity(forum);
    }

    public ForumResponse create(ForumRequest request, User criador) {
        Category category = categoryRepository.findById(request.categoriaId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Forum forum = Forum.builder()
            .titulo(request.titulo())
            .descricao(request.descricao())
            .acesso(request.acesso())
            .criador(criador)
            .categoria(category)
            .build();

        forum = forumRepository.save(forum);
        return ForumResponse.fromEntity(forum);
    }

    public void solicitarParticipacao(Long forumId, User usuario) {
        Forum forum = forumRepository.findById(forumId)
            .orElseThrow(() -> new ResourceNotFoundException("Fórum não encontrado"));

        if (forum.getAcesso() == ForumAccess.PUBLICO) {
            throw new BusinessException("Fórum público não requer solicitação");
        }

        boolean exists = participationRequestRepository
            .existsByUsuarioIdAndForumIdAndStatus(usuario.getId(), forumId, RequestStatus.PENDENTE);
        if (exists) {
            throw new BusinessException("Já existe uma solicitação pendente para este fórum");
        }

        ForumParticipationRequest request = ForumParticipationRequest.builder()
            .usuario(usuario)
            .forum(forum)
            .build();

        participationRequestRepository.save(request);
    }

    public List<ForumParticipationRequest> findSolicitacoesPendentes() {
        return participationRequestRepository.findByStatus(RequestStatus.PENDENTE);
    }

    public void processarSolicitacao(Long solicitacaoId, boolean aprovado) {
        ForumParticipationRequest request = participationRequestRepository.findById(solicitacaoId)
            .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));

        if (aprovado) {
            request.setStatus(RequestStatus.APROVADO);
        } else {
            request.setStatus(RequestStatus.RECUSADO);
        }

        participationRequestRepository.save(request);
    }
}
