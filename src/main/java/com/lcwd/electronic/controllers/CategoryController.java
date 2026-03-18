package com.lcwd.electronic.controllers;

import com.lcwd.electronic.dtos.*;
import com.lcwd.electronic.services.CategoryService;
import com.lcwd.electronic.services.FileService;
import com.lcwd.electronic.services.ProductService;
import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Value("${category.profile.image.path}")
    private String imageUploadPath;
    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.update(categoryDto, categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.delete(categoryId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("category Deleted Successfully with given id!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getCategories(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                          @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                          @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                          @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir) {
        return new ResponseEntity<>(categoryService.getAll(pageNumber,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.get(categoryId), HttpStatus.OK);
    }

    //upload file
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("image") MultipartFile image,@PathVariable String categoryId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        CategoryDto category = categoryService.get(categoryId);
        category.setCoverImage(imageName);
        CategoryDto categoryDto = categoryService.update(category, categoryId);
        ImageResponse response = ImageResponse.builder()
                .imageName(imageName)
                .message("image uploaded !!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.get(categoryId);
        //logger.info("User image name : {}",category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
    @GetMapping("/title/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchAll(@PathVariable String keyword){
        return new ResponseEntity<>(categoryService.searchAll(keyword),HttpStatus.OK);
    }

    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@RequestBody ProductDto productDto,
                                                                @PathVariable String categoryId){
        ProductDto productWithCategory = productService.createProductWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    //update category of products
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProducts(@PathVariable String categoryId,@PathVariable String productId){
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    //return products of given category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> productOfCategory(@PathVariable String categoryId,
                                                                          @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                          @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                          @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                          @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
        PageableResponse<ProductDto> pageableResponse = productService.getAllProductOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
}
