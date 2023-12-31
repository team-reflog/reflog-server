package com.github.teamreflog.reflogserver.team.exception;

public class TeamNameDuplicatedException extends RuntimeException {

    public TeamNameDuplicatedException() {
        super("이미 사용중인 팀 이름입니다.");
    }
}
