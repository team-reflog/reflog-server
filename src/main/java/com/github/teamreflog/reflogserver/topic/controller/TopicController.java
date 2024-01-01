package com.github.teamreflog.reflogserver.topic.controller;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.topic.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.dto.TopicQueryResponse;
import com.github.teamreflog.reflogserver.topic.service.TopicService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<Void> createTopic(
            @Authenticated final AuthPrincipal principal,
            @RequestBody final TopicCreateRequest request) {
        final Long topicId = topicService.createTopic(principal.memberId(), request);

        return ResponseEntity.created(URI.create("/topics/" + topicId)).build();
    }

    @GetMapping
    public List<TopicQueryResponse> queryTopics(@RequestParam("teamId") final Long teamId) {
        return topicService.queryTopics(teamId);
    }
}
