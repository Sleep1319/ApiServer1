package com.api_board.restapiboard.repository;

import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.dto.post.CustomPostRepository;
import com.api_board.restapiboard.dto.post.PostReadCondition;
import com.api_board.restapiboard.dto.post.PostSimpleDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Transactional(readOnly = true) // 1
public class CustomPostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository { // 2

    private final JPAQueryFactory jpaQueryFactory; // 3

    public CustomPostRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<PostSimpleDTO> findAllByCondition(PostReadCondition cond) { // 5
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private List<PostSimpleDTO> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(PostSimpleDTO.class, post.id, post.title, post.member.nickname, post.createdAt))
                        .from(post)
                        .join(post.member)
                        .where(predicate)
                        .orderBy(post.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(post.count()).from(post).where(predicate).fetchOne();
    }

    private Predicate createPredicate(PostReadCondition cond) { // 8
        return new BooleanBuilder()
                .and(orConditionsByEqCategoryIds(cond.getCategoryId()))
                .and(orConditionsByEqMemberIds(cond.getMemberId()));
    }

    private Predicate orConditionsByEqCategoryIds(List<Long> categoryIds) { // 9
        return orConditions(categoryIds, post.category.id::eq);
    }

    private Predicate orConditionsByEqMemberIds(List<Long> memberIds) { // 10
        return orConditions(memberIds, post.member.id::eq);
    }

    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
