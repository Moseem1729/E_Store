package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto, String productId);


    //delete
    void delete(String productId);

    //get single
    ProductDto getSingle(String productId);


    //get all
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get all: live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);


    //search product
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);



    //other methods

    //create product with category// /categories/{categoryId}/products
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    //Assign category to product// /categories/{categoryId}/products/{productId}
    ProductDto updateCategory(String productId, String categoryId);

    //Fetching all products of particular Category

    PageableResponse<ProductDto> getAllOfCategory(String categoryId,  int pageNumber, int pageSize, String sortBy, String sortDir);

}
