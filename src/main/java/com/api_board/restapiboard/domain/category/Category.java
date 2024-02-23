package com.api_board.restapiboard.domain.category;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    /**JPA cascade = CascadeType.Remove 와 OnDeleteAction.CASCADE차이
     * 가장 큰 차이는 JPA에 의한 삭제, DDL에 의한 DB에서의 삭제
     * 전자의 경우 JPA에 의해 외래키를 찾아가며 참조하는 레코드 제거
     * = 참조하고 있는 레코드 수 만큼 delete쿼리 생성
     *
     * 후자의 경우 데이터베이스 자체에 OnDelete Cascade 제약조건이 걸림
     * 이를 통해 참조하는 레코드가 모두 제거 JPA상에선 한개의 delete쿼리가 생성 데이터베이스에서 이를 처리
     *
     * 후자를 선택한 이유 A의 카테고리의 하위 카테고리를 @OneToMany로 JPA 관계를 형성해 줄 필요가 없기 때문
     * @ManyToOne 관계로 부모 카테고리가 무엇인지만 알면 되기에 굳이 새로운 관계를 안만 들기 위해 선택
     */
}
