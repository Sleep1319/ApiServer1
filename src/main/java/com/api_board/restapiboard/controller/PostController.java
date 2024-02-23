package com.api_board.restapiboard.controller;

import com.api_board.restapiboard.aop.AssignMemberId;
import com.api_board.restapiboard.dto.post.PostCreateRequest;
import com.api_board.restapiboard.dto.post.PostReadCondition;
import com.api_board.restapiboard.dto.post.PostUpdateRequest;
import com.api_board.restapiboard.dto.rsponse.Response;
import com.api_board.restapiboard.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Post Controller", tags = "Post")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성한다.")
    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId //aop
    public Response create(@Valid @ModelAttribute PostCreateRequest req) {
        return Response.success(postService.create(req));
    }

    @ApiOperation(value = "게시글 조회", notes = "게시글을 조회한다.")
    @GetMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id) {
        return Response.success(postService.read(id));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제한다.")
    @DeleteMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id) {
        postService.delete(id);
        return Response.success();
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정한다.")
    @PutMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @ApiParam(value = "게시글 id", required = true) @PathVariable Long id,
            @Valid @ModelAttribute PostUpdateRequest req) {
        return Response.success(postService.update(id, req));
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회한다.")
    @GetMapping("/api/posts")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid PostReadCondition cond) {
        return Response.success(postService.readAll(cond));

}
