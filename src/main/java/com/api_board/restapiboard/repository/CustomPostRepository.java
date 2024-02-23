package com.api_board.restapiboard.repository;

import com.api_board.restapiboard.dto.post.PostReadCondition;
import com.api_board.restapiboard.dto.post.PostSimpleDTO;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<PostSimpleDTO> findAllByCondition(PostReadCondition cond);
}
