package com.api_board.restapiboard.service;

import com.api_board.restapiboard.domain.comment.Comment;
import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.dto.comment.CommentCreateRequest;
import com.api_board.restapiboard.dto.comment.CommentDTO;
import com.api_board.restapiboard.dto.comment.CommentReadCondition;
import com.api_board.restapiboard.exception.CommentNotFoundException;
import com.api_board.restapiboard.exception.MemberNotFoundException;
import com.api_board.restapiboard.exception.PostNotFoundException;
import com.api_board.restapiboard.repository.CommentRepository;
import com.api_board.restapiboard.repository.MemberRepository;
import com.api_board.restapiboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;

//    private final AlarmService emailAlarmService;
//    private final AlarmService lineAlarmService;
//    private final AlarmService smsAlarmService;

    public List<CommentDTO> readAll(CommentReadCondition cond) { // 1
        return CommentDTO.toDtoList(
                commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(cond.getPostId())
        );
    }


    @Transactional
    public void create(CommentCreateRequest req) {
        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(req.getPostId()).orElseThrow(PostNotFoundException::new);
        Comment parent = Optional.ofNullable(req.getParentId())
                .map(id -> commentRepository.findById(id).orElseThrow(CommentNotFoundException::new))
                .orElse(null);

        Comment comment = commentRepository.save(new Comment(req.getContent(), member, post, parent));
        comment.publishCreatedEvent(publisher);
    }


    @Transactional

    @PreAuthorize("@commentGuard.check(#id)")
    public void delete(Long id) { // 3
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(commentRepository::delete, comment::delete);
    }

}
