package com.github.teamreflog.reflogserver.reflection.ui;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.reflection.application.ReflectionService;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionQueryResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionTodayQueryRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reflections")
@RequiredArgsConstructor
public class ReflectionController {

    private final ReflectionService reflectionService;

    @PostMapping
    public ResponseEntity<Void> createReflection(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody final ReflectionCreateRequest request,
            @RequestHeader("Time-Zone") final String timezone) {
        final Long reflectionId =
                reflectionService.createReflection(
                        request.setMemberId(authPrincipal.memberId()).setTimezone(timezone));

        return ResponseEntity.created(URI.create("/reflections/" + reflectionId)).build();
    }

    @GetMapping("/today")
    public List<ReflectionQueryResponse> getTodayReflections(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestHeader("Time-Zone") final String timezone) {
        return reflectionService.queryTodayReflections(
                new ReflectionTodayQueryRequest(authPrincipal.memberId(), timezone));
    }
}
