package com.github.teamreflog.reflogserver.reflection.domain;

public interface CrewQueryClient {

    CrewData getCrewDataByCrewId(Long crewId);

    CrewData getCrewDataByMemberIdAndReflectionId(Long memberId, Long reflectionId);
}
