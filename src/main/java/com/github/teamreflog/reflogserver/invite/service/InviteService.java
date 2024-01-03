package com.github.teamreflog.reflogserver.invite.service;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.invite.domain.Invite;
import com.github.teamreflog.reflogserver.invite.domain.InviteRepository;
import com.github.teamreflog.reflogserver.invite.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteQueryResponse;
import com.github.teamreflog.reflogserver.invite.exception.InviteNotExistException;
import com.github.teamreflog.reflogserver.invite.exception.MemberAlreadyInvitedException;
import com.github.teamreflog.reflogserver.invite.exception.MemberAlreadyJoinedException;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.exception.MemberNotExistException;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamMember;
import com.github.teamreflog.reflogserver.team.domain.TeamMemberRepository;
import com.github.teamreflog.reflogserver.team.exception.NicknameDuplicateException;
import com.github.teamreflog.reflogserver.team.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.team.repository.TeamRepository;
import com.github.teamreflog.reflogserver.topic.exception.NotOwnerException;
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
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long inviteMember(final AuthPrincipal authPrincipal, final InviteCreateRequest request) {
        final Team team =
                teamRepository.findById(request.teamId()).orElseThrow(TeamNotExistException::new);

        if (!team.isOwner(authPrincipal.memberId())) {
            throw new NotOwnerException();
        }

        final Member member =
                memberRepository
                        .findByEmail(new MemberEmail(request.email()))
                        .orElseThrow(MemberNotExistException::new);

        if (teamMemberRepository.existsByMemberIdAndTeamId(member.getId(), team.getId())) {
            throw new MemberAlreadyJoinedException();
        }

        if (inviteRepository.existsByMemberIdAndTeamId(member.getId(), team.getId())) {
            throw new MemberAlreadyInvitedException();
        }

        return inviteRepository.save(request.toEntity(member.getId())).getId();
    }

    public List<InviteQueryResponse> queryInvites(final AuthPrincipal authPrincipal) {
        final List<Long> teamIds =
                inviteRepository.findAllByMemberId(authPrincipal.memberId()).stream()
                        .map(Invite::getTeamId)
                        .toList();

        return teamRepository.findAllByIdIn(teamIds).stream()
                .map(InviteQueryResponse::fromEntity)
                .toList();
    }

    public void acceptInvite(
            final AuthPrincipal authPrincipal,
            final Long inviteId,
            final InviteAcceptRequest request) {
        final Invite invite =
                inviteRepository.findById(inviteId).orElseThrow(InviteNotExistException::new);

        if (teamMemberRepository.existsByTeamIdAndNickname(
                invite.getTeamId(), request.nickname())) {
            throw new NicknameDuplicateException();
        }

        // TODO: 팀 멤버도 201로 반환?
        teamMemberRepository.save(
                TeamMember.of(invite.getTeamId(), invite.getMemberId(), request.nickname()));
    }
}
