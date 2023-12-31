package com.github.teamreflog.reflogserver.team.controller;

import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.service.TeamService;
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
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTeam(@RequestBody final TeamCreateRequest request) {
        teamService.createTeam(request);
    }

    @GetMapping("/{id}")
    public TeamQueryResponse queryTeam(@PathVariable("id") final Long teamId) {
        return teamService.queryTeam(teamId);
    }
}
