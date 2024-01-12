package com.github.teamreflog.reflogserver.reflection.application;

import com.github.teamreflog.reflogserver.reflection.application.dto.CommentCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentQueryRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentQueryResponse;
import com.github.teamreflog.reflogserver.reflection.domain.Comment;
import com.github.teamreflog.reflogserver.reflection.domain.CommentRepository;
import com.github.teamreflog.reflogserver.reflection.domain.CrewData;
import com.github.teamreflog.reflogserver.reflection.domain.CrewQueryClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CrewQueryClient crewQueryClient; // TODO: 의존 확인

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
        // TODO: 검증

        final List<Comment> comments =
                commentRepository.findAllByReflectionId(request.reflectionId());

        return comments.stream()
                .map(
                        comment -> {
                            final CrewData crewData =
                                    crewQueryClient.getCrewDataByCrewId(comment.getCrewId());
                            return new CommentQueryResponse(
                                    crewData.nickname(), comment.getContent());
                        })
                .toList();
    }
}
