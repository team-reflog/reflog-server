package com.github.teamreflog.reflogserver.team.exception;

public class TeamNotExistException extends RuntimeException {

    public TeamNotExistException() {
        super("존재하지 않는 팀입니다.");
    }
}
