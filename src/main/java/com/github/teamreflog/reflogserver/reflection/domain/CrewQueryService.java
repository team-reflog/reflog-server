package com.github.teamreflog.reflogserver.reflection.domain;

import java.util.List;

public interface CrewQueryService {

    CrewData getCrewDataById(Long id);

    CrewData getCrewDataByMemberIdAndReflectionId(Long memberId, Long reflectionId);

    List<CrewData> getCrewDatasByMemberIdIsInAndTeamId(List<Long> memberIds, Long teamId);
}
