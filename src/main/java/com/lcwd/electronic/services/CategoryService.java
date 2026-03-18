package com.lcwd.electronic.services;

import com.lcwd.electronic.dtos.CategoryDto;
import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.entities.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto,String categoryId);
    void delete(String categoryId);
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize,String sortBy,String sortDir);
    CategoryDto get(String categoryId);

    List<CategoryDto> searchAll(String keyword);
}
