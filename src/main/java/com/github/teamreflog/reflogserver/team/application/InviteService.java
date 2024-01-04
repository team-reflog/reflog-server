package com.github.teamreflog.reflogserver.team.application;

import com.github.teamreflog.reflogserver.team.application.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteQueryResponse;
import com.github.teamreflog.reflogserver.team.domain.Crew;
import com.github.teamreflog.reflogserver.team.domain.CrewRepository;
import com.github.teamreflog.reflogserver.team.domain.Crews;
import com.github.teamreflog.reflogserver.team.domain.Invite;
import com.github.teamreflog.reflogserver.team.domain.InviteRepository;
import com.github.teamreflog.reflogserver.team.domain.InviteValidator;
import com.github.teamreflog.reflogserver.team.domain.MemberQueryService;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.team.domain.exception.InviteNotExistException;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamNotExistException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteService {

    private final TeamRepository teamRepository;
    private final InviteRepository inviteRepository;
    private final CrewRepository crewRepository;
    private final InviteValidator inviteValidator;
    private final MemberQueryService memberQueryService;

    @Transactional
    public void inviteCrew(final InviteCreateRequest request) {
        inviteValidator.validateTeamOwnerAuthorization(request.memberId(), request.teamId());
        final Long memberId = memberQueryService.getIdByEmail(request.email());

        inviteRepository.save(Invite.of(request.teamId(), memberId));
    }

    public List<InviteQueryResponse> queryInvites(final Long memberId) {
        final List<InviteQueryResponse> responses = new ArrayList<>();
        for (Invite invite : inviteRepository.findAllByMemberId(memberId)) {
            final Team team =
                    teamRepository
                            .findById(invite.getTeamId())
                            .orElseThrow(TeamNotExistException::new);

            responses.add(InviteQueryResponse.fromEntity(invite, team));
        }

        return responses;
    }

    @Transactional
    public void acceptInvite(final InviteAcceptRequest request) {
        final Invite invite =
                inviteRepository
                        .findById(request.inviteId())
                        .orElseThrow(InviteNotExistException::new);
        invite.accept(request.memberId());
        inviteRepository.delete(invite);

        final Crews crews = Crews.from(crewRepository.findAllByTeamId(invite.getTeamId()));
        final Crew crew = Crew.of(invite.getTeamId(), invite.getMemberId(), request.nickname());
        crews.add(crew);
        crewRepository.save(crew);
    }
}
