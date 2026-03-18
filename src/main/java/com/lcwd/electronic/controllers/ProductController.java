package com.lcwd.electronic.controllers;

import com.lcwd.electronic.dtos.*;
import com.lcwd.electronic.services.FileService;
import com.lcwd.electronic.services.ProductService;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imagePath;

    @PreAuthorize("hasRole('ADMIN')")     //only admin call this method
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
       return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,@PathVariable String productId){
        return new ResponseEntity<>(productService.updateProduct(productDto,productId),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        productService.deleteProduct(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Product successfully deleted !!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> GetSingleProduct(@PathVariable String productId){
        return new ResponseEntity<>(productService.getSingleProduct(productId),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    ){
        return new ResponseEntity<>(productService.getAllProducts(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    @GetMapping("/title/{title}")
    public ResponseEntity<PageableResponse<ProductDto>> productSearchByTitle(
            @PathVariable String title,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
            ){
        return new ResponseEntity<>(productService.searchByTitleAllProduct(title,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getLiveProduct(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    ){
        return new ResponseEntity<>(productService.getLiveProducts(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    //upload image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.getSingleProduct(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);

        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName())
                .message("product image successfully updated!!").success(true)
                .status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.getSingleProduct(productId);

        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}
