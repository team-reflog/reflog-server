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
import com.github.teamreflog.reflogserver.team.domain.Invite;
import com.github.teamreflog.reflogserver.team.domain.InviteRepository;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.team.domain.exception.CrewAlreadyJoinedException;
import com.github.teamreflog.reflogserver.team.domain.exception.InviteNotExistException;
import com.github.teamreflog.reflogserver.team.domain.exception.NicknameDuplicateException;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.team.domain.exception.UnauthorizedInviteException;
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
    public void inviteCrew(final AuthPrincipal authPrincipal, final InviteCreateRequest request) {
        final Team team =
                teamRepository.findById(request.teamId()).orElseThrow(TeamNotExistException::new);

        if (!team.isOwner(authPrincipal.memberId())) {
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
    public void acceptInvite(
            final AuthPrincipal authPrincipal,
            final Long inviteId,
            final InviteAcceptRequest request) {
        final Invite invite =
                inviteRepository.findById(inviteId).orElseThrow(InviteNotExistException::new);
        final List<Crew> crews = crewRepository.findAllByTeamId(invite.getTeam().getId());

        if (!invite.isSameMember(authPrincipal.memberId())) {
            throw new UnauthorizedInviteException();
        }

        if (crews.stream().anyMatch(crew -> crew.isSameNickname(request.nickname()))) {
            throw new NicknameDuplicateException();
        }

        if (crews.stream().anyMatch(crew -> crew.isSameMemberId(invite.getMemberId()))) {
            throw new CrewAlreadyJoinedException();
        }

        crewRepository.save(
                Crew.of(invite.getTeam().getId(), invite.getMemberId(), request.nickname()));
        inviteRepository.delete(invite);
    }
}
