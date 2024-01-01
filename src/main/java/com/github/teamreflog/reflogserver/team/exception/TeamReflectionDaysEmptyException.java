package com.github.teamreflog.reflogserver.team.exception;

public class TeamReflectionDaysEmptyException extends RuntimeException {

    public TeamReflectionDaysEmptyException() {
        super("회고일은 최소 하루 이상이어야 합니다.");
    }
}
