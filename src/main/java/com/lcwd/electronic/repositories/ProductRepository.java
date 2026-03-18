package com.lcwd.electronic.repositories;

import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.dtos.ProductDto;
import com.lcwd.electronic.entities.Category;
import com.lcwd.electronic.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findByTitleContaining(String title, Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category,Pageable pageable);
}
