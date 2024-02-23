package com.api_board.restapiboard.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageListDTO {
    private int numberOfElements;
    private boolean hasNext;
    private List<MessageSimpleDTO> messageList;

    public static MessageListDTO messageListDTO(Slice<MessageSimpleDTO> slice) {
        return new MessageListDTO(slice.getNumberOfElements(), slice.hasNext(), slice.getContent());
    }
}
