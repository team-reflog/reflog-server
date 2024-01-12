package com.github.teamreflog.reflogserver.topic.ui;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.auth.domain.Authorities;
import com.github.teamreflog.reflogserver.auth.domain.MemberRole;
import com.github.teamreflog.reflogserver.topic.application.TopicService;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicQueryResponse;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicTodayQueryRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @Authorities(MemberRole.MEMBER)
    @PostMapping
    public ResponseEntity<Void> createTopic(
            @Authenticated final AuthPrincipal principal,
            @RequestBody final TopicCreateRequest request) {
        final Long topicId = topicService.createTopic(request.setMemberId(principal.memberId()));

        return ResponseEntity.created(URI.create("/topics/" + topicId)).build();
    }

    // TODO: 팀에 속한 주제만 조회할 수 있도록 변경
    @Authorities(MemberRole.MEMBER)
    @GetMapping
    public List<TopicQueryResponse> queryTopics(@RequestParam("teamId") final Long teamId) {
        return topicService.queryTopics(teamId);
    }

    @Authorities(MemberRole.MEMBER)
    @GetMapping("/today")
    public List<TopicQueryResponse> queryTodayTopics(
            @Authenticated final AuthPrincipal principal,
            @RequestHeader("Time-Zone") final String timezone) {

        return topicService.queryTodayTopics(
                new TopicTodayQueryRequest(principal.memberId(), timezone));
    }
}
