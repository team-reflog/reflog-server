package com.github.teamreflog.reflogserver.topic.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @PostMapping
    public ResponseEntity<Void> createTopic() {
        return ResponseEntity.created(URI.create("/topics/1")).build();
    }
}
