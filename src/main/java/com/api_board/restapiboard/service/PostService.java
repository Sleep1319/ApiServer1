package com.api_board.restapiboard.service;

import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.post.Image;
import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.dto.post.*;
import com.api_board.restapiboard.exception.PostNotFoundException;
import com.api_board.restapiboard.repository.CategoryRepository;
import com.api_board.restapiboard.repository.MemberRepository;
import com.api_board.restapiboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional
    public PostCreateResponse create(PostCreateRequest req) {
        Post post = postRepository.save(
                PostCreateRequest.toEntity(req, memberRepository, categoryRepository));
        uploadImage(post.getImages(), req.getImages());
        return new PostCreateResponse(post.getId());
    }

    private void uploadImage(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    public PostDTO read(Long id) {
        return PostDTO.postDTO(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    public PostListDTO readAll(PostReadCondition cond) {
        return PostListDTO.postListDTO(
                postRepository.findAllByCondition(cond)
        );
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        deleteImages(post.getImages());
        postRepository.delete(post);
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public PostUpdateResponse update(Long id, PostUpdateRequest req) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException:: new);
        Post.ImageUpdatedResult result = post.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return new PostUpdateResponse(id);
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

}
