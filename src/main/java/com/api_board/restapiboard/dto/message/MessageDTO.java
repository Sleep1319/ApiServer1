package com.api_board.restapiboard.dto.message;

import com.api_board.restapiboard.domain.message.Message;
import com.api_board.restapiboard.dto.MemberDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private MemberDTO sender;
    private MemberDTO receiver;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static MessageDTO toDto(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getContent(),
                MemberDTO.memberDTO(message.getSender()),
                MemberDTO.memberDTO(message.getReceiver()),
                message.getCreatedAt());
    }
}
