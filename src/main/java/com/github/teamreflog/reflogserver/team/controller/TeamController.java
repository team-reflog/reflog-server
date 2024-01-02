package com.github.teamreflog.reflogserver.team.controller;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.dto.TeamInvitationRequest;
import com.github.teamreflog.reflogserver.team.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.service.TeamService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Void> createTeam(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody final TeamCreateRequest request) {
        final Long teamId = teamService.createTeam(authPrincipal, request);

        return ResponseEntity.created(URI.create("/teams/" + teamId)).build();
    }

    @GetMapping("/{id}")
    public TeamQueryResponse queryTeam(@PathVariable("id") final Long teamId) {
        return teamService.queryTeam(teamId);
    }

    @PostMapping("/{id}/invite")
    public void inviteTeamMember(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("id") final Long teamId,
            @RequestBody final TeamInvitationRequest request) {}
}
