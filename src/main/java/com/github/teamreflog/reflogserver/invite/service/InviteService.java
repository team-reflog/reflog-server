package com.github.teamreflog.reflogserver.invite.service;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.invite.domain.Invite;
import com.github.teamreflog.reflogserver.invite.domain.InviteRepository;
import com.github.teamreflog.reflogserver.invite.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteQueryResponse;
import com.github.teamreflog.reflogserver.invite.exception.MemberAlreadyInvitedException;
import com.github.teamreflog.reflogserver.invite.exception.MemberAlreadyJoinedException;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.exception.MemberNotExistException;
import com.github.teamreflog.reflogserver.team.domain.Team;
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
    public void inviteMember(final AuthPrincipal authPrincipal, final InviteCreateRequest request) {
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

        inviteRepository.save(request.toEntity(member.getId()));
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

    public void acceptInvite(final AuthPrincipal authPrincipal, final InviteAcceptRequest request) {
        teamRepository.findById(request.teamId()).orElseThrow(TeamNotExistException::new);
        if (teamMemberRepository.existsByTeamIdAndNickname(request.teamId(), request.nickname())) {
            throw new NicknameDuplicateException();
        }
        teamMemberRepository.save(request.toEntity(authPrincipal.memberId()));
    }
}
