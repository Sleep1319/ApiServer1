package com.api_board.restapiboard.dto.post;

import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.dto.MemberDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Long price;
    private MemberDTO member;
    private List<ImageDTO> images;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static PostDTO postDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                MemberDTO.memberDTO(post.getMember()),
                post.getImages().stream().map(i -> ImageDTO.imageDTO(i)).collect(toList()),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}