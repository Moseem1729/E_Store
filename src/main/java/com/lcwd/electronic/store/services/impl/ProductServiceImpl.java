package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto create(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        Product product = productRepository.save(modelMapper.map(productDto, Product.class));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        //fetch given Id

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product resource not found !!"));
        product.setTitle(productDto.getTitle());
        product.setDescription((productDto.getDescription()));
        product.setPrice((productDto.getPrice()));
        product.setLive(p);

        Product updatedProduct = productRepository.save(product);
        return null;
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(""));
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingle(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(""));

        return modelMapper.map(product, ProductDto.class);
    }
}
