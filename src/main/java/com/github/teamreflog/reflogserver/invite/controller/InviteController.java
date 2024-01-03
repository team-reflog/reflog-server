package com.github.teamreflog.reflogserver.invite.controller;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.invite.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteQueryResponse;
import com.github.teamreflog.reflogserver.invite.service.InviteService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    public ResponseEntity<Void> inviteTeamMember(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody final InviteCreateRequest request) {
        final Long inviteId = inviteService.inviteMember(authPrincipal, request);

        return ResponseEntity.created(URI.create("/invites/" + inviteId)).build();
    }

    @GetMapping
    public List<InviteQueryResponse> queryInvites(
            @Authenticated final AuthPrincipal authPrincipal) {
        return inviteService.queryInvites(authPrincipal);
    }

    @PostMapping("/{id}/accept")
    public void acceptInvite(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("id") final Long inviteId,
            @RequestBody final InviteAcceptRequest request) {
        inviteService.acceptInvite(authPrincipal, inviteId, request);
    }
}
