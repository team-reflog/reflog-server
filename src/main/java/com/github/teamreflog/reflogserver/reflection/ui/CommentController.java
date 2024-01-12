package com.github.teamreflog.reflogserver.reflection.ui;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @PostMapping("/reflections/{reflectionId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment() {}
}
