package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.ProductDto;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto, String productId);


    //delete
    void delete(String productId);

    //get single
    ProductDto getSingle(String productId);


    //get all: search


    //

}
