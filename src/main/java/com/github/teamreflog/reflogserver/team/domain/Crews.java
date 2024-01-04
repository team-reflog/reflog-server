package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.team.domain.exception.CrewAlreadyJoinedException;
import com.github.teamreflog.reflogserver.team.domain.exception.NicknameDuplicateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Crews {

    private final List<Crew> crews;

    public static Crews from(final List<Crew> crews) {
        return new Crews(Collections.unmodifiableList(crews));
    }

    public Crews add(final Crew newCrew) {
        if (crews.stream().anyMatch(crew -> crew.isSameNickname(newCrew.getNickname()))) {
            throw new NicknameDuplicateException();
        }
        if (crews.stream().anyMatch(crew -> crew.isSameMemberId(newCrew.getMemberId()))) {
            throw new CrewAlreadyJoinedException();
        }
        List<Crew> newCrews = new ArrayList<>(this.crews);
        newCrews.add(newCrew);

        return Crews.from(newCrews);
    }
}
