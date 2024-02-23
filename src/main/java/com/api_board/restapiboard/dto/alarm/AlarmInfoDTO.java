package com.api_board.restapiboard.dto.alarm;

import com.api_board.restapiboard.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmInfoDTO {
    private MemberDTO target;
    private String message;
}
