package com.api_board.restapiboard.repository;

import com.api_board.restapiboard.domain.message.Message;
import com.api_board.restapiboard.dto.message.MessageSimpleDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m left join fetch m.sender left join fetch m.receiver where m.id = :id")
    Optional<Message> findWithSenderAndReceiverById(Long id); // 1

    // 2
    @Query("select new com.api_board.restapiboard.dto.message.MessageSimpleDTO(m.id, m.content, m.receiver.nickname, m.createdAt) " +
            "from Message m left join m.receiver " +
            "where m.sender.id = :senderId and m.id < :lastMessageId and m.deletedBySender = false order by m.id desc")
    Slice<MessageSimpleDTO> findAllBySenderIdOrderByMessageIdDesc(Long senderId, Long lastMessageId, Pageable pageable);

    // 2
    @Query("select new com.api_board.restapiboard.dto.message.MessageSimpleDTO(m.id, m.content, m.sender.nickname, m.createdAt) " +
            "from Message m left join m.sender " +
            "where m.receiver.id = :receiverId and m.id < :lastMessageId and m.deletedByReceiver = false order by m.id desc")
    Slice<MessageSimpleDTO> findAllByReceiverIdOrderByMessageIdDesc(Long receiverId, Long lastMessageId, Pageable pageable);
}
