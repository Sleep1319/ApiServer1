package com.api_board.restapiboard.event;

import com.api_board.restapiboard.dto.MemberDTO;
import com.api_board.restapiboard.dto.alarm.AlarmInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Data
@AllArgsConstructor
public class CommentCreatedEvent {
    private MemberDTO publisher; //댓글 작성자
    private MemberDTO postWriter; //게시글 작성자
    private MemberDTO parentWriter; //상위 댓글 작성자
    private String content; //댓글 내용

    @PostConstruct
    public void postConstruct() {
        alarmServices.add(emailAlarmService);
        alarmServices.add(lineAlarmService);
        alarmServices.add(smsAlarmService);
    }

    @TransactionalEventListener //이벤트를 발행하던 트랜잭션의 흐름에 따라, 이벤트를 제어
    @Async //작업 끝났음에도 불구하고 응답이 지연되는 등의 문제가 발생, 활성화를 위해 AsyncConfig 작성
    public void handleAlarm(CommentCreatedEvent event) { // 5
        log.info("CommentCreatedListener.handleAlarm");
        String message = generateAlarmMessage(event);
        if(isAbleToSendToPostWriter(event)) alarmTo(event.getPostWriter(), message);
        if(isAbleToSendToParentWriter(event)) alarmTo(event.getParentWriter(), message);
    }

    private void alarmTo(MemberDTO memberDTO, String message) { // 6
        alarmServices.stream().forEach(alarmService -> alarmService.alarm(new AlarmInfoDTO(memberDTO, message)));
    }

    private boolean isAbleToSendToPostWriter(CommentCreatedEvent event) { // 7
        if(!isSameMember(event.getPublisher(), event.getPostWriter())) {
            if(hasParent(event)) return !isSameMember(event.getPostWriter(), event.getParentWriter());
            return true;
        }
        return false;
    }

    private boolean isAbleToSendToParentWriter(CommentCreatedEvent event) { // 8
        return hasParent(event) && !isSameMember(event.getPublisher(), event.getParentWriter());
    }

    private boolean isSameMember(MemberDTO a, MemberDTO b) {
        return Objects.equals(a.getId(), b.getId());
    }

    private boolean hasParent(CommentCreatedEvent event) {
        return event.getParentWriter().getId() != null;
    }

    private String generateAlarmMessage(CommentCreatedEvent event) { // 9
        return event.getPublisher().getNickname() + " : " + event.getContent();
    }
}
