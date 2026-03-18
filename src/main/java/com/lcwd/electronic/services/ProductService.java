package com.lcwd.electronic.services;

import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.dtos.ProductDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto,String productId);
    void deleteProduct(String productId);
    ProductDto getSingleProduct(String productId);
    PageableResponse<ProductDto> getAllProducts(int pageNumber,int pageSize,String sortBy,String sortDir);
    PageableResponse<ProductDto> searchByTitleAllProduct(String title,int pageNumber,int pageSize,String sortBy,String sortDir);
    PageableResponse<ProductDto> getLiveProducts(int pageNumber,int pageSize,String sortBy,String sortDir);

    //create product with Category
    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);
    //update category of product
    ProductDto updateCategory(String productId,String categoryId);

    //return products of given category
    PageableResponse<ProductDto> getAllProductOfCategory(String category, int pageNumber, int pageSize, String sortBy, String sortDir);
}
