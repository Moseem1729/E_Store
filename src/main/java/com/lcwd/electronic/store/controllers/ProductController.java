package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.payload.ApiResponse;
import com.lcwd.electronic.store.payload.ImageResponse;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){

        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    
    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto){

        ProductDto updatedProduct = productService.update(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
    
    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId){

        productService.delete(productId);
        return new ResponseEntity<>(new ApiResponse("Product deleted successfully !!", HttpStatus.OK, true), HttpStatus.OK);
    }
    
    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId){

        ProductDto productDto = productService.getSingle(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY_TITLE, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY_TITLE, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //search by title
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @PathVariable String query,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY_TITLE, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
            ) throws IOException {

        String fileName = fileService.uploadImage(image, imagePath);
        ProductDto productDto = productService.getSingle(productId);
        productDto.setProductImageName(fileName);
        logger.info("Product image Name: " + productDto.getProductImageName());
        ProductDto updatedProduct = productService.update(productDto, productId);
        logger.info("Product image Name: " + updatedProduct.getProductImageName());

        ImageResponse response = ImageResponse.builder()
                .imageName(updatedProduct.getProductImageName())
                .message("Product image is successfully uploaded !!")
                .httpStatus(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/image/{productId}")
    public void serveProductImage(
            @PathVariable String productId,
            HttpServletResponse response
    ) throws IOException {
        logger.info("Sending request to FileService for serving image with id: {}", productId);

        ProductDto productDto = productService.getSingle(productId);
        logger.info("product image name ; {}" , productDto.getProductImageName());
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        logger.info("Successfully served image with name: {}", productDto.getProductImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
