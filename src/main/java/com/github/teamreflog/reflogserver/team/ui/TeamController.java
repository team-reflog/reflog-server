package com.github.teamreflog.reflogserver.team.ui;

import com.github.teamreflog.reflogserver.auth.annotation.Authenticated;
import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.team.application.TeamService;
import com.github.teamreflog.reflogserver.team.application.dto.CrewQueryResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryResponse;
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

    @GetMapping("/{id}/members")
    public List<CrewQueryResponse> queryCrews(@PathVariable("id") final Long teamId) {
        return teamService.queryCrews(teamId);
    }
}
