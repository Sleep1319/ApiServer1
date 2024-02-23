package com.api_board.restapiboard.service;

import com.api_board.restapiboard.domain.message.Message;
import com.api_board.restapiboard.dto.message.MessageCreateRequest;
import com.api_board.restapiboard.dto.message.MessageDTO;
import com.api_board.restapiboard.dto.message.MessageListDTO;
import com.api_board.restapiboard.dto.message.MessageReadCondition;
import com.api_board.restapiboard.exception.MessageNotFoundException;
import com.api_board.restapiboard.repository.MemberRepository;
import com.api_board.restapiboard.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MessageListDTO readAllBySender(MessageReadCondition cond) { // 1
        return MessageListDTO.messageListDTO(
                messageRepository.findAllBySenderIdOrderByMessageIdDesc(cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize()))
        );
    }

    public MessageListDTO readAllByReceiver(MessageReadCondition cond) { // 1
        return MessageListDTO.messageListDTO(
                messageRepository.findAllByReceiverIdOrderByMessageIdDesc(cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize()))
        );
    }

    @PreAuthorize("@messageGuard.check(#id)")
    public MessageDTO read(Long id) { // 2
        return MessageDTO.toDto(
                messageRepository.findWithSenderAndReceiverById(id).orElseThrow(MessageNotFoundException::new)
        );
    }

    @Transactional
    public void create(MessageCreateRequest req) { // 3
        messageRepository.save(MessageCreateRequest.toEntity(req, memberRepository));
    }

    @Transactional
    @PreAuthorize("@messageGuard.check(#id)")
    public void deleteBySender(Long id) { // 4
        delete(id, Message::deleteBySender);
    }

    @Transactional
    @PreAuthorize("@messageGuard.check(#id)")
    public void deleteByReceiver(Long id) { // 4
        delete(id, Message::deleteByReceiver);
    }

    private void delete(Long id, Consumer<Message> delete) { // 5
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        delete.accept(message);
        if(message.isDeletable()) {
            messageRepository.delete(message);
        }
    }
}
