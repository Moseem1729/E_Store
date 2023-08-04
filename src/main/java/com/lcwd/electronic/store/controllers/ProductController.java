package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.payload.ApiResponse;
import com.lcwd.electronic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
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
}
