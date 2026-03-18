package com.lcwd.electronic.repositories;

import com.lcwd.electronic.dtos.CategoryDto;
import com.lcwd.electronic.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String> {

    List<Category> findByTitleContaining(String keyword);
}
