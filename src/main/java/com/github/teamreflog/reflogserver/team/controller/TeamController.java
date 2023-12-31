package com.github.teamreflog.reflogserver.team.controller;

import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTeam(@RequestBody final TeamCreateRequest request) {}
}
