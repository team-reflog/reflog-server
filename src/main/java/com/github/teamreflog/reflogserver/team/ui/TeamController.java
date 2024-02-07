package com.github.teamreflog.reflogserver.team.ui;

import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.auth.domain.Authenticated;
import com.github.teamreflog.reflogserver.auth.domain.Authorities;
import com.github.teamreflog.reflogserver.auth.domain.MemberRole;
import com.github.teamreflog.reflogserver.team.application.TeamService;
import com.github.teamreflog.reflogserver.team.application.dto.CrewQueryResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryDetailRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryDetailResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamReflectionQueryResponse;
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

    @Authorities(MemberRole.MEMBER)
    @PostMapping
    public ResponseEntity<Void> createTeam(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody final TeamCreateRequest request) {
        final Long teamId = teamService.createTeam(request.setMemberId(authPrincipal.memberId()));

        return ResponseEntity.created(URI.create("/teams/" + teamId)).build();
    }

    @Authorities(MemberRole.MEMBER)
    @GetMapping("/{id}")
    public TeamQueryResponse queryTeam(@PathVariable("id") final Long teamId) {
        return teamService.queryTeam(teamId);
    }

    @Authorities(MemberRole.MEMBER)
    @GetMapping
    public List<TeamQueryResponse> queryTeams(@Authenticated final AuthPrincipal authPrincipal) {
        return teamService.queryTeams(authPrincipal.memberId());
    }

    @Authorities(MemberRole.MEMBER)
    @GetMapping("/{id}/detail")
    public TeamQueryDetailResponse queryTeamDetails(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("id") final Long teamId) {
        return teamService.queryTeamDetails(
                new TeamQueryDetailRequest(authPrincipal.memberId(), teamId));
    }

    @Authorities(MemberRole.MEMBER)
    @GetMapping("/{id}/members") // TODO: crews로 바꾸고 CrewController로 빼기
    public List<CrewQueryResponse> queryCrews(@PathVariable("id") final Long teamId) {
        return teamService.queryCrews(teamId);
    }

    @Authorities(MemberRole.MEMBER)
    @GetMapping("/{id}/reflections/today")
    public TeamReflectionQueryResponse queryTodayTeamReflections(
            @PathVariable("id") final Long teamId) {
        return teamService.queryTodayTeamReflections(teamId);
    }
}
