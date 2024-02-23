package com.api_board.restapiboard.domain.comment;

import com.api_board.restapiboard.domain.common.EntityDate;
import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.dto.MemberDTO;
import com.api_board.restapiboard.event.CommentCreatedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Comment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private boolean deleted; // 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member; // 2

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post; // 3

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parent; // 4

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>(); // 5

    public Comment(String content, Member member, Post post, Comment parent) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.parent = parent;
        this.deleted = false;
    }

    public Optional<Comment> findDeletableComment() { // 6
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableCommentByParent());
    }

    public void delete() { // 7
        this.deleted = true;
    }

    private Comment findDeletableCommentByParent() { // 1
        if (isDeletedParent()) {
            Comment deletableParent = getParent().findDeletableCommentByParent();
            if(getParent().getChildren().size() == 1) return deletableParent;
        }
        return this;
    }

    private boolean hasChildren() {
        return getChildren().size() != 0;
    }

    private boolean isDeletedParent() { // 2
        return getParent() != null && getParent().isDeleted();
    }

    public void publishCreatedEvent(ApplicationEventPublisher publisher) {
        publisher.publishEvent(
                new CommentCreatedEvent(
                        MemberDTO.memberDTO(getMember()),
                        MemberDTO.memberDTO(getPost().getMember()),
                        Optional.ofNullable(getParent()).map(Comment::getMember).map(MemberDTO::memberDTO).orElseGet(MemberDTO::empty),
                        getContent()
                )
        );
    }
}
