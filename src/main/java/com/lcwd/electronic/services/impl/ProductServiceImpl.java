package com.lcwd.electronic.services.impl;

import com.lcwd.electronic.dtos.ImageResponse;
import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.dtos.ProductDto;
import com.lcwd.electronic.entities.Category;
import com.lcwd.electronic.entities.Product;
import com.lcwd.electronic.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.helper.Helper;
import com.lcwd.electronic.repositories.CategoryRepository;
import com.lcwd.electronic.repositories.ProductRepository;
import com.lcwd.electronic.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.path}")
    private String imagesPath;
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        //set the id
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        //set the date
        productDto.setAddedDate(new Date());

        Product product = mapper.map(productDto, Product.class);
        return mapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id !!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setAddedDate(new Date());
        product.setPrice(product.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        return mapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id !!"));

        //    images/users/abc.png
        String fullPath=imagesPath+product.getProductImageName();
        try {
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
           // logger.info("User Image not found in folder");
            ex.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id !!"));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitleAllProduct(String title, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByTitleContaining(title, pageable);
        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }

    //create product with category  //ise hm category controller ke through request maarenge http://localhost:9090/categories/{categoryId}/products
    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
        //fetch the category from db
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found!!"));
        //save

        Product product = mapper.map(productDto, Product.class);

        String id = UUID.randomUUID().toString();
        product.setProductId(id);                     //aap chao to upar wale create product ki trh bhe set kr skte ho productDto mai ya phir aise bhi kr skte ho
        //set added date

        product.setAddedDate(new Date());
        //set category
        product.setCategory(category);

        Product saveProduct = productRepository.save(product);

        return mapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        //fetch the product from db
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product of id is not found"));
        //fetch the category from db
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category of id is not found"));
        //set 
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category of id is not found"));

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepository.findByCategory(category, pageable);

        return Helper.getPageableResponse(page, ProductDto.class);
    }
}
