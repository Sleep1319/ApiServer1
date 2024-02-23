package com.api_board.restapiboard.service;

import com.api_board.restapiboard.dto.alarm.AlarmInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LineAlarmService implements AlarmService {
    @Override
    public void alarm(AlarmInfoDTO infoDTO) {
        log.info("{} 에게 라인 전송 = {}", infoDTO.getTarget().getNickname(), infoDTO.getMessage());
    }
}
