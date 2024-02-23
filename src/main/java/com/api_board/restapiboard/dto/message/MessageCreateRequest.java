package com.api_board.restapiboard.dto.message;

import com.api_board.restapiboard.domain.message.Message;
import com.api_board.restapiboard.exception.MemberNotFoundException;
import com.api_board.restapiboard.repository.MemberRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@ApiModel(value = "쪽지 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequest {
    @ApiModelProperty(value = "쪽지", notes = "쪽지를 입력해주세요", required = true, example = "my message")
    @NotBlank(message = "쪽지를 입력해주세요.")
    private String content;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "수신자 아이디", notes = "수신자 아이디를 입력해주세요", example = "7")
    @NotNull(message = "수신자 아이디를 입력해주세요.")
    @Positive(message = "올바른 수신자 아이디를 입력해주세요.")
    private Long receiverId;

    public static Message toEntity(MessageCreateRequest req, MemberRepository memberRepository) {
        return new Message(
                req.content,
                memberRepository.findById(req.memberId).orElseThrow(MemberNotFoundException::new),
                memberRepository.findById(req.receiverId).orElseThrow(MemberNotFoundException::new)
        );
    }
}
