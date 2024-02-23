package com.api_board.restapiboard.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @EntityListeners
 * 엔티티의 생명주기에 대한 이벤트를 리스닝하고 처리하는 역할
 *
 * AuditingEntityListener
 * 엔티티의 변경 이벤트나 생성 이벤트 등을 자동으로 처리할 수 있게 한다.
 * 주로 생성일자(created date), 수정일자(modified date) 등을 자동으로 설정하기 위해 사용
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class EntityDate {
    //updatable = false: 해당 필드가 생성된후 값을 수정하지 못하게 만드는 목적
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime modifiedAt;
}
