package com.api_board.restapiboard.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 이넘 타입으로 바로 롤타입 선언해서 쓰는게 아닌 따로 엔티티 선언해서 사용 하는중
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType roleType;

    public Role(RoleType roeType){
        this.roleType = roeType;
    }
}
