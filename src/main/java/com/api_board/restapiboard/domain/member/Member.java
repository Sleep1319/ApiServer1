package com.api_board.restapiboard.domain.member;

import com.api_board.restapiboard.domain.common.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 기존 롤 타입 조회는 멤버를 조회하는 쿼리 생성
 * 멤버에 멤버롤을 조회하는 쿼리 생성
 * 멤버롤의 롤을 조회하는 쿼리 생성으로 흘러감
 * 또 연관 되어 있는 엔티티가 있으면 n번 조회로 간다
 * 그래서 멤버 롤과 롤을 조인 하는 방법으로 변경
 *  fetch join, fetch 전략을 EAGER로 설정하는 등 다양한 방식이 있다
 *  Entity Graph를 이용
 *  하나의 아이디가 여러개의 아이디를 가지고 있으면 동일한 아이디가 중복해서 조회되는 문제가 있다
 *  하나의 권한만 주어지니 현 상황에선 중복 조회는 없다
 */

/**
 * 31번 attributeNode에 함께 조회할 엔티티 필드명 적어준다
 * roles는 OneToMany관계에 있지만 엔티티 그래프에서 N+1문제를 일어나지 않게 조절
 * 실질적인 Role의 타입 명까지 확인하려면, MemberRole의 role도 함께 조회
 * 이를 위해 subgraph에, 또 다른 그래프의 이름 Member.roles.role을 지정
 * 서브 그래프를 이용하면, 연관 엔티티의 연관 엔티티까지 함께 조회
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//기본 생성자를 protected로 설정함
@NamedEntityGraph(name = "Member.roles",
        attributeNodes = @NamedAttributeNode(value = "roles", subgraph = "Member.roles.role"),
        subgraphs = @NamedSubgraph(name = "Member.roles.role", attributeNodes = @NamedAttributeNode("role")))
public class Member extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRole> roles;

    public Member(String email, String password, String username, String nickname, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles.stream().map(r -> new MemberRole(this, r)).collect(Collectors.toSet());
    }

    public void updateNickname(String nickname) { // 6
        this.nickname = nickname;
    }
}
