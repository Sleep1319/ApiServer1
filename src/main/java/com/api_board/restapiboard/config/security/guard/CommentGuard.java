package com.api_board.restapiboard.config.security.guard;

import com.api_board.restapiboard.domain.comment.Comment;
import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.member.RoleType;
import com.api_board.restapiboard.exception.AccessDeniedException;
import com.api_board.restapiboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentGuard extends Guard{
    private final CommentRepository commentRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return commentRepository.findById(id)
                .map(Comment::getMember)
                .map(Member::getId)
                .filter(memberId -> memberId.equals(AuthHelper.extractMemberId()))
                .isPresent();
    }
}
