package com.github.teamreflog.reflogserver.reflection;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reflections")
public class ReflectionController {

    @PostMapping
    public ResponseEntity<Void> createReflection() {
        final Long reflectionId = 1L;

        return ResponseEntity.created(URI.create("/reflections/" + reflectionId)).build();
    }
}
