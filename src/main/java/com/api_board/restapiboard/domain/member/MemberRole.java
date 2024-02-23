package com.api_board.restapiboard.domain.member;

import lombok.*;

import javax.persistence.*;

/**
 * @IdClass 활용 이유
 * composite key로 사용된 필드에 접근할 때,
 * 여러 번 getter를 사용해서 접근해야하기 때문에 불필요하게 코드가 길어지고, 가독성이 떨어진다는 단점이 있음
 * @IdClass를 이용 하여  composite key로 사용될 필드들을 어노테이션으로 선언만 해두면 되기 때문에,
 * key 필드에 접근할 때 불필요하게 getter를 연속해서 사용할 필요가 없어짐
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(MemberRoleId.class)
public class MemberRole {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
