package com.lcwd.electronic.services.impl;

import com.lcwd.electronic.dtos.CategoryDto;
import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.entities.Category;
import com.lcwd.electronic.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.helper.Helper;
import com.lcwd.electronic.repositories.CategoryRepository;
import com.lcwd.electronic.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${category.profile.image.path}")
    private String imagePath;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        String id = UUID.randomUUID().toString();
        categoryDto.setCategoryId(id);

        Category category = categoryRepository.save(mapper.map(categoryDto, Category.class));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id !!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found with given id !!"));

        //    images/users/abc.png
        String fullPath = imagePath + category.getCoverImage();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            //    logger.info("User Image not found in folder");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        categoryRepository.delete(category);

    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;

    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found with given id"));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchAll(String keyword) {
        return categoryRepository.findByTitleContaining(keyword).stream().map(title -> mapper.map(title, CategoryDto.class)).collect(Collectors.toList());
    }

}
