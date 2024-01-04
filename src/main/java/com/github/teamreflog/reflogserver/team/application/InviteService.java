package com.github.teamreflog.reflogserver.team.application;

import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.domain.exception.MemberNotExistException;
import com.github.teamreflog.reflogserver.team.application.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteQueryResponse;
import com.github.teamreflog.reflogserver.team.domain.Crew;
import com.github.teamreflog.reflogserver.team.domain.CrewRepository;
import com.github.teamreflog.reflogserver.team.domain.Crews;
import com.github.teamreflog.reflogserver.team.domain.Invite;
import com.github.teamreflog.reflogserver.team.domain.InviteRepository;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.team.domain.exception.InviteNotExistException;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
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
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;

    @Transactional
    public void inviteCrew(final InviteCreateRequest request) {
        final Team team =
                teamRepository.findById(request.teamId()).orElseThrow(TeamNotExistException::new);

        if (!team.isOwner(request.memberId())) {
            throw new NotOwnerException();
        }

        final Member member =
                memberRepository
                        .findByEmail(new MemberEmail(request.email()))
                        .orElseThrow(MemberNotExistException::new);

        team.addInvite(Invite.of(team, member.getId()));
    }

    public List<InviteQueryResponse> queryInvites(final AuthPrincipal authPrincipal) {
        return inviteRepository.findAllByMemberId(authPrincipal.memberId()).stream()
                .map(InviteQueryResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void acceptInvite(final InviteAcceptRequest request) {
        final Invite invite =
                inviteRepository
                        .findById(request.inviteId())
                        .orElseThrow(InviteNotExistException::new);
        invite.accept(request.memberId());
        inviteRepository.delete(invite);

        final Crews crews = Crews.from(crewRepository.findAllByTeamId(invite.getTeam().getId()));
        final Crew crew =
                Crew.of(invite.getTeam().getId(), invite.getMemberId(), request.nickname());
        crews.add(crew);
        crewRepository.save(crew);
    }
}
