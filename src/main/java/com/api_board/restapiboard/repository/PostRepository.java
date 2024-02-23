package com.api_board.restapiboard.repository;

import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.dto.post.CustomPostRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    @Query("select p from Post p join fetch p.member where p.id = :id")
    Optional<Post> findByIdWithMember(Long id);
}
