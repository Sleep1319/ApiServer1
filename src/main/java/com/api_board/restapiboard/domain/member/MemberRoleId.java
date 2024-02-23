package com.api_board.restapiboard.domain.member;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Serializable java제공 인터페이스 객체 직렬화를 지원
 * 객체 직렬화: 객체를 바이트 스트림으로 변환 하여 저장하거나 네트워크를 통해 전송하게 도와주는 메커니즘
 */
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberRoleId implements Serializable {
    private Member member;
    private Role role;
}
