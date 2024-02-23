package com.api_board.restapiboard.dto.comment;

import com.api_board.restapiboard.domain.comment.Comment;
import com.api_board.restapiboard.exception.CommentNotFoundException;
import com.api_board.restapiboard.exception.MemberNotFoundException;
import com.api_board.restapiboard.exception.PostNotFoundException;
import com.api_board.restapiboard.repository.CommentRepository;
import com.api_board.restapiboard.repository.MemberRepository;
import com.api_board.restapiboard.repository.PostRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.Optional;

@ApiModel(value = "댓글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

    @ApiModelProperty(value = "댓글", notes = "댓글을 입력해주세요", required = true, example = "my comment")
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "게시글 아이디", notes = "게시글 아이디를 입력해주세요", example = "7")
    @NotNull(message = "게시글 아이디를 입력해주세요.")
    @Positive(message = "올바른 게시글 아이디를 입력해주세요.")
    private Long postId;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "부모 댓글 아이디", notes = "부모 댓글 아이디를 입력해주세요", example = "7")
    private Long parentId;

//    public static Comment toEntity(CommentCreateRequest req, MemberRepository memberRepository, PostRepository postRepository, CommentRepository commentRepository) {
//        return new Comment(
//                req.content,
//                memberRepository.findById(req.memberId).orElseThrow(MemberNotFoundException::new),
//                postRepository.findById(req.postId).orElseThrow(PostNotFoundException::new),
//                Optional.ofNullable(req.parentId)
//                        .map(id -> commentRepository.findById(id).orElseThrow(CommentNotFoundException::new))
//                        .orElse(null)
//        );
//    }
}
