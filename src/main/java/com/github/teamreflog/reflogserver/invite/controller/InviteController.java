package com.github.teamreflog.reflogserver.invite.controller;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.invite.dto.InvitationRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteResponse;
import com.github.teamreflog.reflogserver.invite.service.InviteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void inviteTeamMember(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody final InvitationRequest request) {
        inviteService.inviteMember(authPrincipal, request);
    }

    @GetMapping
    public List<InviteResponse> queryInvites(@Authenticated final AuthPrincipal authPrincipal) {
        return inviteService.queryInvites(authPrincipal);
    }

    @PostMapping("/accept")
    public void acceptInvitation(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody final InviteAcceptRequest request) {
        inviteService.acceptInvite(authPrincipal, request);
    }
}
