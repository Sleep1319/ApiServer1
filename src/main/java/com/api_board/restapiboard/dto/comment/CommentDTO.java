package com.api_board.restapiboard.dto.comment;

import com.api_board.restapiboard.domain.comment.Comment;
import com.api_board.restapiboard.dto.MemberDTO;
import com.api_board.restapiboard.helper.NestedConvertHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private MemberDTO member;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<CommentDTO> children;

    public static List<CommentDTO> toDtoList(List<Comment> comments) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                comments,
                c -> new CommentDTO(c.getId(), c.isDeleted() ? null : c.getContent(), c.isDeleted() ? null : MemberDTO.memberDTO(c.getMember()), c.getCreatedAt(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }
}
