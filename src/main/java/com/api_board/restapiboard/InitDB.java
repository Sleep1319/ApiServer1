package com.api_board.restapiboard;

import com.api_board.restapiboard.domain.category.Category;
import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.member.Role;
import com.api_board.restapiboard.domain.member.RoleType;
import com.api_board.restapiboard.domain.post.Post;
import com.api_board.restapiboard.exception.RoleNotFoundException;
import com.api_board.restapiboard.repository.CategoryRepository;
import com.api_board.restapiboard.repository.MemberRepository;
import com.api_board.restapiboard.repository.PostRepository;
import com.api_board.restapiboard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")
public class InitDB {
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");
        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
        initPost();
    }

    private void initRole() {
        roleRepository.saveAll(
                Stream.of(RoleType.values()).map(Role::new).collect(Collectors.toList())
        );
    }

    private void initTestAdmin() {
        memberRepository.save(
                new Member("admin@admin.com", passwordEncoder.encode("1234"), "admin", "관리자1",
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new)))
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member("member@member.com", passwordEncoder.encode("1234"), "member", "사용자별명1",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))),
                        new Member("member2@member.com", passwordEncoder.encode("1234"), "member2", "사용자별명2",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new)))
                )
        );
    }

    private void initCategory() {
        Category c1 = categoryRepository.save(new Category("category1", null));
        Category c2 = categoryRepository.save(new Category("category2", c1));
        Category c3 = categoryRepository.save(new Category("category3", c1));
        Category c4 = categoryRepository.save(new Category("category4", c2));
        Category c5 = categoryRepository.save(new Category("category5", c2));
        Category c6 = categoryRepository.save(new Category("category6", c4));
        Category c7 = categoryRepository.save(new Category("category7", c3));
        Category c8 = categoryRepository.save(new Category("category8", null));
    }

    private void initPost() {
        Member member = memberRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);
        IntStream.range(0, 100000)
                .forEach(i -> postRepository.save(
                        new Post("title" + i, "content" + i, Long.valueOf(i), member, category, List.of())
                ));
    }
}
