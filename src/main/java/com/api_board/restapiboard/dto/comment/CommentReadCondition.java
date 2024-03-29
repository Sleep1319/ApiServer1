package com.api_board.restapiboard.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadCondition {
    @NotNull(message = "게시글 번호를 입력해 주세요.")
    @PositiveOrZero(message = "올바른 게시글 번호를 입력해 주세요. 0 이상")
    private Long postId;
}
