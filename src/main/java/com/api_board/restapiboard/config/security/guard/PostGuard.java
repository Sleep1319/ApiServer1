package com.api_board.restapiboard.config.security.guard;

import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.member.RoleType;
import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 서비스는 인증 서비스가 아니다. 접근 제어 정책 코드는 비즈니스 로직으로 보기 힘들기 때문에, 서비스 로직에 해당 코드를 넣는 것은 애매하다
 * 서비스 로직에 접근 제어 코드를 삽입한다면, 서비스 코드는 이러한 코드로 범벅이 될 것이고, 각 메소드마다 유사한 제어 코드가 중복되고 말 것
 * 이를 방지하기 위해 Security에서 Guard를 이용한 접근 제어
 */

@Component
@RequiredArgsConstructor
public class PostGuard extends Guard {
    private final PostRepository postRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return postRepository.findById(id)
                .map(Post::getMember)
                .map(Member::getId)
                .filter(memberId -> memberId.equals(AuthHelper.extractMemberId()))
                .isPresent();
    }

}
