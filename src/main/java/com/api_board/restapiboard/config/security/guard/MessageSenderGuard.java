package com.api_board.restapiboard.config.security.guard;

import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.member.RoleType;
import com.api_board.restapiboard.domain.message.Message;
import com.api_board.restapiboard.exception.AccessDeniedException;
import com.api_board.restapiboard.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageSenderGuard extends Guard {
    private final MessageRepository messageRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return messageRepository.findById(id)
                .map(Message::getSender)
                .map(Member::getId)
                .filter(senderId -> senderId.equals(AuthHelper.extractMemberId()))
                .isPresent();
    }
}
