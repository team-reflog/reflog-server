package com.github.teamreflog.reflogserver.member.ui;

import com.github.teamreflog.reflogserver.member.application.MemberService;
import com.github.teamreflog.reflogserver.member.application.dto.MemberJoinRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody final MemberJoinRequest request) {
        final Long memberId = memberService.createMember(request);

        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }
}
