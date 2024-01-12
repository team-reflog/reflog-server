package com.github.teamreflog.reflogserver.reflection.domain;

public interface CrewQueryClient {

    CrewData getCrewDataById(Long id);

    CrewData getCrewDataByMemberIdAndReflectionId(Long memberId, Long reflectionId);
}
