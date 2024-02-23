package com.api_board.restapiboard.repository;

import com.api_board.restapiboard.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c left join c.parent p order by p.id asc nulls first , c.id asc ")
    List<Category>findAllOrderByParentIdAscNullFirstCategoryAsc();
}
