package com.api_board.restapiboard.service;

import com.api_board.restapiboard.domain.category.Category;
import com.api_board.restapiboard.dto.category.CategoryDTO;
import com.api_board.restapiboard.dto.category.CategoryCreateRequest;
import com.api_board.restapiboard.exception.CategoryNotFoundException;
import com.api_board.restapiboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> readAll() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullFirstCategoryAsc();
        return CategoryDTO.toDTOList(categories);
    }

    @Transactional
    public void create(CategoryCreateRequest req) {
        categoryRepository.save(CategoryCreateRequest.toEntity(req, categoryRepository));
    }

    @Transactional
    public void delete(Long id) {
        if(notExistsCategory(id)) throw new CategoryNotFoundException();
        categoryRepository.deleteById(id);
    }

    private boolean notExistsCategory(Long id) {
        return !categoryRepository.existsById(id);
    }
}
