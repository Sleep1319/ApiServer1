package com.api_board.restapiboard.repository;

import com.api_board.restapiboard.domain.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
