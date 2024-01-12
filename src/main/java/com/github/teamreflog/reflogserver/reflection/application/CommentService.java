package com.github.teamreflog.reflogserver.reflection.application;

import com.github.teamreflog.reflogserver.reflection.application.dto.CommentCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentQueryRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentQueryResponse;
import com.github.teamreflog.reflogserver.reflection.domain.Comment;
import com.github.teamreflog.reflogserver.reflection.domain.CommentRepository;
import com.github.teamreflog.reflogserver.reflection.domain.CommentValidator;
import com.github.teamreflog.reflogserver.reflection.domain.CrewData;
import com.github.teamreflog.reflogserver.reflection.domain.CrewQueryClient;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CrewQueryClient crewQueryClient;
    private final CommentValidator commentValidator;

    @Transactional
    public Long createComment(final CommentCreateRequest request) {
        final CrewData crewData =
                crewQueryClient.getCrewDataByMemberIdAndReflectionId(
                        request.memberId(), request.reflectionId());

        return commentRepository
                .save(Comment.of(crewData.crewId(), request.reflectionId(), request.content()))
                .getId();
    }

    @Transactional(readOnly = true)
    public List<CommentQueryResponse> queryComments(final CommentQueryRequest request) {
        commentValidator.validateAccess(request.memberId(), request.reflectionId());

        final List<CommentQueryResponse> responses = new ArrayList<>();
        for (final Comment comment :
                commentRepository.findAllByReflectionId(request.reflectionId())) {
            final CrewData crewData = crewQueryClient.getCrewDataByCrewId(comment.getCrewId());
            responses.add(new CommentQueryResponse(crewData.nickname(), comment.getContent()));
        }

        return responses;
    }
}
