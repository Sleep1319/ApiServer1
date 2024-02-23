package com.api_board.restapiboard.dto.category;

import com.api_board.restapiboard.domain.category.Category;
import com.api_board.restapiboard.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private List<CategoryDTO> children;

    /**
     * 첫번쨰 인자: 계층형 구조로 변환할 엔티티 목록
     * 두번째 인자: 엔티티를 DTO로 변환하는 함수
     * 세번째 인자: 엔티티의 부모를 반환하는 함수
     * 네번째 인자: 엔티티의 ID를 반환하는 함수
     * 다섯번째 인자: DTO의 자식 목록을 반환하는 함수
     */
    public static List<CategoryDTO> toDTOList(List<Category> categories) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                categories,
                c -> new CategoryDTO(c.getId(), c.getName(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }
}
