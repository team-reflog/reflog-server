package com.github.teamreflog.reflogserver.reflection.domain;

public interface CrewQueryService {

    CrewData getCrewDataById(Long id);

    CrewData getCrewDataByMemberIdAndReflectionId(Long memberId, Long reflectionId);
}
