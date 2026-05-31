package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.ForumParticipationRequest;
import com.api.ecnomia_api.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ForumParticipationRequestRepository extends JpaRepository<ForumParticipationRequest, Long> {
    List<ForumParticipationRequest> findByForumIdAndStatus(Long forumId, RequestStatus status);
    List<ForumParticipationRequest> findByStatus(RequestStatus status);
    Optional<ForumParticipationRequest> findByUsuarioIdAndForumId(Long usuarioId, Long forumId);
    boolean existsByUsuarioIdAndForumIdAndStatus(Long usuarioId, Long forumId, RequestStatus status);
}
