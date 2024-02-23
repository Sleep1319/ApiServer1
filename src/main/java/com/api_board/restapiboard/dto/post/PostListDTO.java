package com.api_board.restapiboard.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PostListDTO {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<PostSimpleDTO> postList;

    public static PostListDTO postListDTO(Page<PostSimpleDTO> page) {
        return new PostListDTO(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}