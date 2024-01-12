package com.github.teamreflog.reflogserver.reflection.ui;

import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.auth.domain.Authenticated;
import com.github.teamreflog.reflogserver.auth.domain.Authorities;
import com.github.teamreflog.reflogserver.auth.domain.MemberRole;
import com.github.teamreflog.reflogserver.reflection.application.CommentService;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentQueryRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentQueryResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Authorities(MemberRole.MEMBER)
    @PostMapping("/reflections/{reflectionId}/comments")
    public ResponseEntity<Void> createComment(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("reflectionId") final Long reflectionId,
            @RequestBody final CommentCreateRequest request) {
        final Long commentId =
                commentService.createComment(
                        request.setMemberId(authPrincipal.memberId())
                                .setReflectionId(reflectionId));

        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @Authorities(MemberRole.MEMBER)
    @GetMapping("/reflections/{reflectionId}/comments")
    public List<CommentQueryResponse> queryComments(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("reflectionId") final Long reflectionId) {
        return commentService.queryComments(
                new CommentQueryRequest(authPrincipal.memberId(), reflectionId));
    }
}
