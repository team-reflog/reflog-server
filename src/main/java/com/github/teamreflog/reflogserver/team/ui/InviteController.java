package com.github.teamreflog.reflogserver.team.ui;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.team.application.InviteService;
import com.github.teamreflog.reflogserver.team.application.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteQueryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void inviteCrew(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody final InviteCreateRequest request) {
        inviteService.inviteCrew(request.setMemberId(authPrincipal.memberId()));
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
        inviteService.acceptInvite(
                request.setMemberId(authPrincipal.memberId()).setInviteId(inviteId));
    }
}
