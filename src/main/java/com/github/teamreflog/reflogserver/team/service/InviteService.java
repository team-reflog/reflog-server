package com.github.teamreflog.reflogserver.team.service;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.exception.MemberNotExistException;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamInvite;
import com.github.teamreflog.reflogserver.team.domain.TeamInviteRepository;
import com.github.teamreflog.reflogserver.team.domain.TeamMemberRepository;
import com.github.teamreflog.reflogserver.team.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.team.dto.InviteResponse;
import com.github.teamreflog.reflogserver.team.dto.TeamInvitationRequest;
import com.github.teamreflog.reflogserver.team.exception.MemberAlreadyInvitedException;
import com.github.teamreflog.reflogserver.team.exception.MemberAlreadyJoinedException;
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
    private final TeamInviteRepository teamInviteRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void inviteMember(
            final AuthPrincipal authPrincipal, final TeamInvitationRequest request) {
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

        if (teamInviteRepository.existsByMemberIdAndTeamId(member.getId(), team.getId())) {
            throw new MemberAlreadyInvitedException();
        }

        teamInviteRepository.save(request.toEntity(member.getId()));
    }

    public List<InviteResponse> queryInvites(final AuthPrincipal authPrincipal) {
        final List<Long> teamIds =
                teamInviteRepository.findAllByMemberId(authPrincipal.memberId()).stream()
                        .map(TeamInvite::getTeamId)
                        .toList();

        return teamRepository.findAllByIdIn(teamIds).stream()
                .map(InviteResponse::fromEntity)
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
